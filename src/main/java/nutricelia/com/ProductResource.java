package nutricelia.com;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.inject.Inject;
import java.util.List;

@Path("/products")
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProduct(@PathParam("id") String id) {
        return productService.fetchAndSaveMercadonaProducts();
    }
}