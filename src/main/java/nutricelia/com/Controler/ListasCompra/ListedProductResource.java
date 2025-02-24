package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import nutricelia.com.Model.ListedProduct;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/ListedProductResource")
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
