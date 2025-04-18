package nutricelia.com.Controler;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import nutricelia.com.Model.Forum;
import nutricelia.com.Model.User;

@Path("/foro")
public class ForumResource {

    @Inject
    ForumService forumService;

    @Inject
    Template crearForoView;

    @GET
    @Path("/crear")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance mostrarFormulario() {
        return crearForoView.instance();
    }

    @POST
    @Path("/crear")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Uni<Response> crearForo(
            @FormParam("nombre") String nombre,
            @FormParam("mensaje") String mensaje,
            @FormParam("email") String emailUsuario) {

        return forumService.crearForo(nombre, mensaje, emailUsuario)
                .onItem().transform(foro ->
                        Response.ok("Foro creado con éxito!").build()
                ).onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Error al crear el foro: " + throwable.getMessage())
                                .build()
                );
    }
}