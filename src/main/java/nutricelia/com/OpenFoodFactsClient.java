package nutricelia.com;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;

@RegisterRestClient(baseUri = "https://world.openfoodfacts.org/api/v2")
@Produces(MediaType.APPLICATION_JSON)
public interface OpenFoodFactsClient {

    @GET
    @Path("/search")
    ProductSearchResponse searchProductsByStore(
            @QueryParam("stores_tags") String store,
            @QueryParam("fields") String fields,
            @QueryParam("page_size") int pageSize,
            @QueryParam("page") int page

            );



}