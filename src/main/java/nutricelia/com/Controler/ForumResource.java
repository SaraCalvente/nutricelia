package nutricelia.com.Controler;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtException;
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
    @Path("/lista")
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
    @Authenticated
    public Uni<Response> crearForo(@FormParam("nombre") String nombre,
                                   @FormParam("mensaje") String mensaje) {

        return forumService.crearForo(nombre, mensaje)
                .onItem().transform(foro -> {
                    TemplateInstance templateInstance = crearForoView.data("foro", foro);
                    String renderedHtml = templateInstance.render();
                    return Response.ok(renderedHtml).build();
                })
                .onFailure().recoverWithItem(error -> {
                    TemplateInstance templateInstance = crearForoView.data("error", "Error al crear foro: " + error.getMessage());
                    String renderedHtml = templateInstance.render();
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(renderedHtml).build();
                });
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

    @POST
    @Path("/{nombre}/crear")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Authenticated
    public Uni<Response> crearMensaje(@PathParam("nombre") String nombreForo,
                                      @FormParam("mensaje") String mensaje) {

        return forumService.crearMensajeEnForo(nombreForo, mensaje)
                .onItem().transformToUni(foro ->
                        forumService.obtenerMensajesDelForo(nombreForo)
                                .onItem().transformToUni(mensajes ->
                                        Uni.createFrom().completionStage(
                                                verForoView.data("foro", foro).data("mensajes", mensajes).renderAsync()
                                        )
                                )
                                .onItem().transform(renderedHtml ->
                                        Response.ok(renderedHtml).build()
                                )
                )
                .onFailure().recoverWithItem(error -> {
                    TemplateInstance templateInstance = verForoView.data("error", "Error al enviar mensaje: " + error.getMessage());
                    String renderedHtml = templateInstance.render();
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(renderedHtml).build();
                });
    }



}