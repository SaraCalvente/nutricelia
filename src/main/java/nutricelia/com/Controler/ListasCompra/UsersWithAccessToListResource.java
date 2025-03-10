package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import nutricelia.com.Model.UsersWithAccessToList;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
@Path("/UsersWithAccesToListResource")
public class UsersWithAccessToListResource {
    private final UsersWithAccessToListService usersWithAccessToListService;
    @Inject
    public UsersWithAccessToListResource(UsersWithAccessToListService usersWithAccessToListService) {
        this.usersWithAccessToListService = usersWithAccessToListService;
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

    @GET
    @Path("{id}")
    public Uni<UsersWithAccessToList> get(@PathParam("id") long id) {
        return usersWithAccessToListService.findById(id);
    }

    /*
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Uni<BuyList> update(@PathParam("id") long id, BuyList buyList) {
        buyList.id = (int) id;      //Puede que halla que cambiar el tipo del id
        return buyListService.update(buyList);
    }
    */
    @DELETE
    @Path("{id}")
    public Uni<Void> delete(@PathParam("id") long id) {
        return usersWithAccessToListService.delete(id);
    }
    /*
    @GET
    @Path("self")
    public Uni<BuyList> getCurrentUser() {
        return buyListService.getCurrentUser();
    }

     */

}

