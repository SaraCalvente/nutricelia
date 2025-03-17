package nutricelia.com.Controler;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import nutricelia.com.Model.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class UserService {

    public Uni<User> findByName(String name) {
        return User.find("name", name).firstResult();
    }

    public Uni<User> findByEmail(String email) {
        return User.find("email", email).firstResult();
    }

    public Uni<List<User>> list() {
        return User.listAll();
    }

    @ReactiveTransactional
    public Uni<User> create(User user) {
        return findByEmail(user.email)
                .onItem().ifNotNull().failWith(() -> new WebApplicationException("El usuario ya existe", 409))
                .onItem().ifNull().continueWith(user)
                .chain(u -> {
                    user.setPassword(BcryptUtil.bcryptHash(user.getPassword())); // Cifrar contraseña
                    return user.persistAndFlush();
                });
    }

    @ReactiveTransactional
    public Uni<User> update(User user) {
        return findByEmail(user.email)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Usuario no encontrado", 404))
                .chain(existingUser -> {
                    existingUser.nombre = user.nombre;
                    return existingUser.persistAndFlush(); // Guardar cambios en la BD
                });
    }

    @ReactiveTransactional
    public Uni<Void> delete(String email) {
        return findByEmail(email)
                .chain(user -> {
                    if (user == null) {
                        return Uni.createFrom().voidItem(); // No se encontró el usuario, devolver vacío
                    }
                    return user.delete() // Borrar al usuario
                            .onItem().ignore().andContinueWithNull(); // Ignorar el resultado de la eliminación
                });
    }

    public static boolean matches(User user, String password) {
        return BcryptUtil.matches(password, user.getPassword());
    }

}
