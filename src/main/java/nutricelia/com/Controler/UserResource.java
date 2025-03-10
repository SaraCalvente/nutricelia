package nutricelia.com.Controler;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import nutricelia.com.Model.User;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.util.List;

@Path("/user")
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Uni<List<User>> get() {
        return userService.list();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<User> create(User user) {
        return userService.create(user);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{email: .+}") //Es posible que esto genere errores por el formato email
    public Uni<User> update(@PathParam("email") String name, User user) {
        user.nombre = name;
        return userService.update(user);
    }

    @DELETE
    @Path("{email: .+}")
    public Uni<Void> delete(@PathParam("email") String email) {
        return userService.delete(email);
    }

}
