package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import nutricelia.com.Model.BuyList;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
@Path("/soloDiosLoSabe")
public class BuyListResource {
    private final BuyListService buyListService;
    @Inject
    public BuyListResource(BuyListService buyListService) {
        this.buyListService = buyListService;
    }
    @GET
    public Uni<List<BuyList>> get() {
        return buyListService.list();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<BuyList> create(BuyList buyList) {
        return buyListService.create(buyList);
    }
    @GET
    @Path("{id}")
    public Uni<BuyList> get(@PathParam("id") long id) {
        return buyListService.findById(id);
    }

    /*
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Uni<BuyList> update(@PathParam("id") long id, BuyList buyList) {
        buyList.id = (int) id;      //Puede que halla que cambiar el tipo del id
        return buyListService.update(buyList);
    }
    @DELETE
    @Path("{id}")
    public Uni<Void> delete(@PathParam("id") long id) {
        return buyListService.delete(id);
    }
    @GET
    @Path("self")
    public Uni<BuyList> getCurrentUser() {
        return buyListService.getCurrentUser();
    }

     */

}
