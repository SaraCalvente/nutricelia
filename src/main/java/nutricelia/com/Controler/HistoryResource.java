package nutricelia.com.Controler;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import nutricelia.com.Model.History;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Path("/HistoryResource")
public class HistoryResource {
    private final HistoryService historyService;

    private String decodificar(String encodedEmail){
        try {
            return URLDecoder.decode(encodedEmail, StandardCharsets.UTF_8);
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    @Inject
    public HistoryResource(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GET
    public Uni<List<History>> get() {
        return historyService.list();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<History> create(History history) {
        return historyService.create(history);
    }

    @GET
    @Path("{id}")
    public Uni<History> get(@PathParam("id") long id) {
        return historyService.findById(id);
    }

    @GET
    @Path("/list/{email}")
    public Uni<List<History>> getByUserMail(@PathParam("email") String email) {
        String decodedEmail = decodificar(email);
        return historyService.findByUserMail(email);
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
        return historyService.delete(id);
    }
    /*
    @GET
    @Path("self")
    public Uni<BuyList> getCurrentUser() {
        return buyListService.getCurrentUser();
    }

     */

}