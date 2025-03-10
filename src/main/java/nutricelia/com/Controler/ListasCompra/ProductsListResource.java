package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import nutricelia.com.Model.ProductsList;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/ListedProductResource")
public class ProductsListResource {
    private final ProductsListService productsListService;
    @Inject
    public ProductsListResource(ProductsListService productsListService) {
        this.productsListService = productsListService;
    }

    @GET
    public Uni<List<ProductsList>> get() {
        return productsListService.list();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<ProductsList> create(ProductsList productsList) {
        return productsListService.create(productsList);
    }

    @GET
    @Path("{id}")
    public Uni<ProductsList> get(@PathParam("id") long id) {
        return productsListService.findById(id);
    }

    @GET
    @Path("/list/{id_lista}")
    public Uni<List<ProductsList>> getByListId(@PathParam("id_lista") int id_lista) {
        return productsListService.findByListId(id_lista);
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
        return productsListService.delete(id);
    }
    /*
    @GET
    @Path("self")
    public Uni<BuyList> getCurrentUser() {
        return buyListService.getCurrentUser();
    }

     */

}
