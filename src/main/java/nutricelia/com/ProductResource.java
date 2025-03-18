package nutricelia.com;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;


@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {


    @Inject
    ProductService productService;

    @GET
    @Path("/search")
    public ProductSearchResponse searchProducts(@QueryParam("store") String store) {
        return productService.searchProductsByStore1("mercadona");
    }



}