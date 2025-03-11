package nutricelia.com.Controler.ListasCompra;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import nutricelia.com.Model.UsersWithAccessToList;
import nutricelia.com.Model.UsersWithAccessToListId;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@Path("/usersWithAccesToList")
public class UsersWithAccessToListResource {
    private final UsersWithAccessToListService usersWithAccessToListService;
    @Inject
    public UsersWithAccessToListResource(UsersWithAccessToListService usersWithAccessToListService) {
        this.usersWithAccessToListService = usersWithAccessToListService;
    }

    private String decodificar(String encodedEmail){
        try {
            return URLDecoder.decode(encodedEmail, StandardCharsets.UTF_8);
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    @GET
    @Path("/{email}/{id_lista}")
    public Uni<UsersWithAccessToList> get(@PathParam("email") String email, @PathParam("id_lista") int id_lista) {
        String decodedEmail = decodificar(email);
        UsersWithAccessToListId id = new UsersWithAccessToListId();
        id.email=decodedEmail;
        id.id_lista=id_lista;

        return usersWithAccessToListService.findById(id);
    }

    @GET
    @Path("/{id_lista}")
    public Uni<List<UsersWithAccessToList>> get(@PathParam("id_lista") int id_lista) {
        return usersWithAccessToListService.findByListId(id_lista);
    }

    @GET
    public Uni<List<UsersWithAccessToList>> get() {
        return usersWithAccessToListService.list();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<UsersWithAccessToList> create(UsersWithAccessToList usersWithAccessToList) {
        return usersWithAccessToListService.create(usersWithAccessToList);
    }

    @DELETE
    @Path("/{email}/{id_lista}")
    public Uni<Void> delete(@PathParam("email") String email, @PathParam("id_lista") int id_lista) {
        String decodedEmail = decodificar(email);
        UsersWithAccessToListId id = new UsersWithAccessToListId();
        id.email=decodedEmail;
        id.id_lista=id_lista;

        return usersWithAccessToListService.delete(id);
    }
}

