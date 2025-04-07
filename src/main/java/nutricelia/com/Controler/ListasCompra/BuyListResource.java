package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import nutricelia.com.Model.BuyList;
import org.hibernate.ObjectNotFoundException;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
@Path("/buyListResource")
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
    public Uni<BuyList> get(@PathParam("id") int id) {
        return buyListService.findById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<BuyList> update(@PathParam("id") int id, BuyList updatedList) {
        return buyListService.update(id, updatedList);
    }

    @DELETE
    @Path("{id}")
    public Uni<Void> delete(@PathParam("id") int id) {
        return buyListService.delete(id);
    }

}
