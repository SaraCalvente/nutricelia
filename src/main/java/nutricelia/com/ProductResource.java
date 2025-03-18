package nutricelia.com;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    public List<Product> getProducts(@QueryParam("category") String category,
                                     @QueryParam("brand") String brand) {
        return productService.fetchProducts(category, brand);
    }
}