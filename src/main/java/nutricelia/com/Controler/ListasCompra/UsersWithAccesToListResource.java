package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import nutricelia.com.Model.UsersWithAccesToList;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
@Path("/UsersWithAccesToListResource")
public class UsersWithAccesToListResource {
    private final UsersWithAccesToListService usersWithAccesToListService;
    @Inject
    public UsersWithAccesToListResource(UsersWithAccesToListService usersWithAccesToListService) {
        this.usersWithAccesToListService = usersWithAccesToListService;
    }

    @GET
    public Uni<List<UsersWithAccesToList>> get() {
        return usersWithAccesToListService.list();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<UsersWithAccesToList> create(UsersWithAccesToList usersWithAccesToList) {
        return usersWithAccesToListService.create(usersWithAccesToList);
    }

    @GET
    @Path("{id}")
    public Uni<UsersWithAccesToList> get(@PathParam("id") long id) {
        return usersWithAccesToListService.findById(id);
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
        return usersWithAccesToListService.delete(id);
    }
    /*
    @GET
    @Path("self")
    public Uni<BuyList> getCurrentUser() {
        return buyListService.getCurrentUser();
    }

     */

}

