package nutricelia.com;


import nutricelia.com.OpenFoodFactsResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://world.openfoodfacts.org/api/v2")
public interface OpenFoodFactsClient {

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    OpenFoodFactsResponse searchProducts(@QueryParam("categories_tags") String category,
                                         @QueryParam("brands_tags") String brand,
                                         @QueryParam("fields") String fields);
}