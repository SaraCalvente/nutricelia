package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import nutricelia.com.Model.ListedProduct;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/soloDiosLoSabe")
public class ListedProductResource {
    private final ListedProductService listedProductService;
    @Inject
    public ListedProductResource(ListedProductService listedProductService) {
        this.listedProductService = listedProductService;
    }

    @GET
    public Uni<List<ListedProduct>> get() {
        return listedProductService.list();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<ListedProduct> create(ListedProduct listedProduct) {
        return listedProductService.create(listedProduct);
    }

    @GET
    @Path("{id}")
    public Uni<ListedProduct> get(@PathParam("id") long id) {
        return listedProductService.findById(id);
    }

    @GET
    @Path("/list/{id_lista}")
    public Uni<List<ListedProduct>> getByListId(@PathParam("id_lista") int id_lista) {
        return listedProductService.findByListId(id_lista);
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
        return listedProductService.delete(id);
    }
    /*
    @GET
    @Path("self")
    public Uni<BuyList> getCurrentUser() {
        return buyListService.getCurrentUser();
    }

     */

}
