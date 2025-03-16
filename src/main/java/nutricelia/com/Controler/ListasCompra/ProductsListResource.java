package nutricelia.com.Controler.ListasCompra;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import nutricelia.com.Model.ProductsList;
import nutricelia.com.Model.ProductsListId;
import org.jboss.resteasy.reactive.ResponseStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/productsList")
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
    @Path("/{id_lista}/{id_producto}")
    public Uni<ProductsList> get(@PathParam("id_lista") int id_lista, @PathParam("id_producto") int id_producto) {
        ProductsListId id = new ProductsListId();
        id.id_lista=id_lista;
        id.id_producto=id_producto;

        return productsListService.findById(id);
    }

    @GET
    @Path("/list/{id_lista}")
    public Uni<List<ProductsList>> getByListId(@PathParam("id_lista") int id_lista) {
        return productsListService.findByListId(id_lista);
    }

    @DELETE
    @Path("/{id_lista}/{id_producto}")
    public Uni<Void> delete(@PathParam("id_lista") int id_lista, @PathParam("id_producto") int id_producto) {
        ProductsListId id = new ProductsListId();
        id.id_lista=id_lista;
        id.id_producto=id_producto;

        return productsListService.delete(id);
    }
}
