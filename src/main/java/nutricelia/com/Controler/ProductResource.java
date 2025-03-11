package nutricelia.com.Controler;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import nutricelia.com.Model.BuyList;
import nutricelia.com.Model.Product;
import nutricelia.com.Model.User;
import org.jose4j.http.Get;

import java.util.List;


@Path("/product")
public class ProductResource {

    private final ProductService productService;

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }


    @GET
    @Path("/{id}")
    public Uni<Product> getProduct(@PathParam("id") int id) {
        return productService.findById(id);
    }

}
