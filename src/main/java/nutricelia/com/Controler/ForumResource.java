package nutricelia.com.Controler;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.smallrye.mutiny.Uni;

import java.util.List;


@Path("/foro")
@Produces(MediaType.TEXT_HTML)

public class ForumResource {

    @Inject
    ForumService forumService;

    @Inject
    Template crearForoView;

    @Inject
    Template verForoView;

    @Inject
    Template listarForosView;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Uni<Response> listarForos() {
        return forumService.listarForos()
                .onItem().transform(foros -> listarForosView.data("foros", foros))
                .onItem().transformToUni(templateInstance -> {
                    String renderHtml = templateInstance.render();
                    return Uni.createFrom().item(Response.ok(renderHtml).build());
                });
    }

    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Uni<TemplateInstance> crearForo(
            @FormParam("nombre") String nombre,
            @FormParam("mensaje") String mensaje,
            @FormParam("email") String emailUsuario) {

        return forumService.crearForo(nombre, mensaje, emailUsuario)
                .onItem().transform(foro -> verForoView.data("foro", foro).data("mensajes", List.of()))
                .onFailure().recoverWithItem(error ->
                        crearForoView.data("error", "Error al crear foro: " + error.getMessage())
                );
    }

    @GET
    @Path("/{nombre}")
    @Produces(MediaType.TEXT_HTML)
    public Uni<Response> verForo(@PathParam("nombre") String nombre) {
        return forumService.obtenerForo(nombre)
                .onItem().transformToUni(foro -> {
                    if (foro == null) {
                        return Uni.createFrom().item(crearForoView.data("error", "Foro no encontrado").render());
                    }
                    return forumService.obtenerMensajesDelForo(nombre)
                            .onItem().transform(mensajes ->
                                    verForoView.data("foro", foro).data("mensajes", mensajes).render()
                            );
                })
                .onItem().transform(renderedHtml ->
                        Response.ok(renderedHtml).build()
                );
    }

    // Enviar mensaje al foro
    @POST
    @Path("/{foroNombre}/mensaje")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Uni<Response> enviarMensaje(
            @PathParam("foroNombre") String foroNombre,
            @FormParam("mensaje") String mensaje,
            @FormParam("email") String emailUsuario) {

        return forumService.agregarMensajeAForo(foroNombre, mensaje, emailUsuario)
                .onItem().transformToUni(foro -> forumService.obtenerMensajesDelForo(foroNombre)
                        .onItem().transform(mensajes ->
                                verForoView.data("foro", foro).data("mensajes", mensajes).render()
                        ))
                .onItem().transform(renderedHtml ->
                        Response.ok(renderedHtml).build()
                )
                .onFailure().recoverWithItem(error ->
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Error al enviar mensaje: " + error.getMessage())
                                .build()
                );
    }
}