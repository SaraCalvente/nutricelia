package nutricelia.com.auth;

import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import nutricelia.com.Controler.UserService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;

@ApplicationScoped
public class AuthService {
    private final String issuer;
    private final UserService userService;

    @Inject
    public AuthService(@ConfigProperty(name = "mp.jwt.verify.issuer") String issuer,
            UserService userService) {
        this.issuer = issuer;
        this.userService = userService;
    }

    // Anotación por problemas en la sesión Mutiny.
    // https://stackoverflow.com/questions/77528402/missing-withsession-with-quarkus-resteasy-reactive-causes-java-lang-illegalstat
    @WithSessionOnDemand
    public Uni<String> authenticate(AuthRequest authRequest) {
        return userService.findByEmail(authRequest.email()) // Usamos email como identificador
                .onItem()
                .transform(user -> {
                    // Verificar que el usuario existe y que la contraseña es correcta
                    if (user == null || !UserService.matches(user, authRequest.password())) {
                        throw new AuthenticationFailedException("Invalid credentials");
                    }

                    // Generar el JWT con el email del usuario (clave primaria) y sin roles
                    return Jwt.issuer(issuer)
                            .upn(user.email) // Usamos el email como identificador único
                            .expiresIn(Duration.ofHours(1L)) // Expiración en 1 hora
                            .sign(); // Firmar el JWT
                });
    }
}

