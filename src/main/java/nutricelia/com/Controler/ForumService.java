package nutricelia.com.Controler;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.Forum;
import nutricelia.com.Model.ForumMessages;
import nutricelia.com.Model.User;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ForumService {

    public Uni<Forum> crearForo(String nombre, String mensaje, String emailUsuario) {
        return Panache.withTransaction(() ->
                User.find("email", emailUsuario).firstResult()
                        .onItem().ifNull().failWith(() -> new IllegalArgumentException("Usuario no encontrado"))
                        .onItem().transformToUni(usuario -> {
                            Forum foro = new Forum();
                            foro.setNombre(nombre);
                            foro.setUsuario((User) usuario);

                            return foro.<Forum>persist()
                                    .onItem().transformToUni(foroPersistido -> {
                                        ForumMessages forumMessage = new ForumMessages();
                                        forumMessage.setNombreForo(foroPersistido.getNombre());
                                        forumMessage.setMensaje(mensaje);
                                        forumMessage.setFechaPublicacion(LocalDateTime.now());
                                        forumMessage.setUsuario((User) usuario);
                                        return forumMessage.persist()
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


}