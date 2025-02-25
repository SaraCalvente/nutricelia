package nutricelia.com;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/api")
public interface IOpenFoodFacts {
    @GET
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Product getProduct(@PathParam("id") String id);
}