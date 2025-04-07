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
    @Path("/{id}")
    public Uni<BuyList> get(@PathParam("id") int id) {
        return buyListService.findById(id);
    }

    /*
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Uni<BuyList> update(@PathParam("id") long id, BuyList buyList) {
        buyList.id = (int) id;      //Puede que halla que cambiar el tipo del id
        return buyListService.update(buyList);
    } */

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Object> update(@PathParam("id") int id, BuyList updatedBuyList) { //En lugar de Uni<buyList> es Uni<Object> por el ObjectNotFoundExp
        return buyListService.findById(id)
                .onItem().ifNotNull().transformToUni(buyList -> {
                    buyList.nombre = updatedBuyList.nombre;
                    return buyList.persistAndFlush();
                })
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "BuyList"));
    }

    @DELETE
    @Path("{id}")
    public Uni<Void> delete(@PathParam("id") int id) {
        return buyListService.delete(id);
    }

    /*
    @GET
    @Path("self")
    public Uni<BuyList> getCurrentUser() {
        return buyListService.getCurrentUser();
    }

     */

}
