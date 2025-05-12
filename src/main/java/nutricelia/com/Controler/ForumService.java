package nutricelia.com.Controler;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nutricelia.com.Model.Forum;
import nutricelia.com.Model.ForumMessages;
import nutricelia.com.Model.ForumMessagesId;
import nutricelia.com.Model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class ForumService {

    @Inject
    SecurityIdentity securityIdentity;

    public Uni<Forum> crearForo(String nombre, String mensaje) {
        String correo = securityIdentity.getPrincipal().getName();
        return Panache.withTransaction(() ->
                User.find("email", correo).firstResult()
                        .onItem().ifNull().failWith(() -> new IllegalArgumentException("Usuario no encontrado"))
                        .onItem().transformToUni(usuario -> {
                            Forum foro = new Forum();
                            foro.setNombre(nombre);
                            foro.setUsuario((User) usuario);

                            return foro.<Forum>persistAndFlush()
                                    .onItem().transformToUni(foroPersistido -> {
                                        LocalDateTime ahora = LocalDateTime.now();

                                        ForumMessages forumMessage = new ForumMessages();
                                        ForumMessagesId id = new ForumMessagesId();
                                        id.setEmail(correo);
                                        id.setNombreForo(foroPersistido.getNombre());
                                        id.setFechaPublicacion(ahora);

                                        forumMessage.setId(id);
                                        forumMessage.setMensaje(mensaje);
                                        forumMessage.setUsuario((User) usuario);
                                        forumMessage.setForo(foroPersistido);

                                        return forumMessage.persistAndFlush()
                                                .onFailure().invoke(Throwable::printStackTrace)
                                                .onItem().transform(ignored -> foroPersistido);
                                    });
                        })
        );
    }

    public Uni<Forum> obtenerForo(String nombre) {
        return Forum.findById(nombre);
    }

    public Uni<List<ForumMessages>> obtenerMensajesDelForo(String nombre) {
        return ForumMessages.find("id.nombreForo = ?1 ORDER BY id.fechaPublicacion", nombre).list();    }

    public Uni<List<Forum>> listarForos() {
        return Forum.listAll();
    }

    public Uni<Forum> crearMensajeEnForo(String nombreForo, String mensaje) {
        String correo = securityIdentity.getPrincipal().getName();

        return Panache.withTransaction(() ->
                User.find("email", correo).firstResult()
                        .onItem().ifNull().failWith(() -> new IllegalArgumentException("Usuario no encontrado"))
                        .onItem().transformToUni(usuario ->
                                Forum.find("nombre", nombreForo).firstResult()  // Buscar el foro por nombre
                                        .onItem().ifNull().failWith(() -> new IllegalArgumentException("Foro no encontrado"))
                                        .onItem().transformToUni(foro -> {
                                            Forum forum = (Forum) foro;

                                            ForumMessages forumMessage = new ForumMessages();
                                            forumMessage.setMensaje(mensaje);
                                            forumMessage.setForo(forum);
                                            forumMessage.setUsuario((User) usuario);

                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                            String fechaFormateada = LocalDateTime.now().format(formatter);
                                            LocalDateTime fechaPublicacion = LocalDateTime.parse(fechaFormateada, formatter);

                                            ForumMessagesId id = new ForumMessagesId();
                                            id.setEmail(correo);
                                            id.setNombreForo(forum.getNombre());
                                            id.setFechaPublicacion(fechaPublicacion);

                                            forumMessage.setId(id);

                                            return forumMessage.persistAndFlush()
                                                    .onItem().transform(ignored -> forum);
                                        })
                        )
        );
    }

}