package nutricelia.com.Controler;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.*;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.Forum;
import nutricelia.com.Model.ForumMessages;
import nutricelia.com.Model.Product;
import nutricelia.com.Model.User;
import org.hibernate.ObjectNotFoundException;

import java.util.concurrent.Executor;
import java.util.function.Predicate;

@ApplicationScoped

public class ForumService
{

    public Uni<Forum> crearForo(String nombre, String mensaje, String emailUsuario) {
        return User.find("email", emailUsuario).firstResult()
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Usuario no encontrado"))
                .onItem().transformToUni(usuario -> {
                    Forum foro = new Forum();
                    foro.setNombre(nombre);

                    return foro.<Forum>persist()
                            .onItem().transformToUni(foroPersistido -> {
                                ForumMessages forumMessage = new ForumMessages();
                                forumMessage.setNombreForo(foroPersistido.getNombre());
                                forumMessage.setMensaje(mensaje);
                                forumMessage.setFechaPublicacion(java.time.LocalDateTime.now());
                                forumMessage.setUsuario((User) usuario);
                                return forumMessage.persist()
                                        .onItem().transform(ignored -> foroPersistido);
                            });
                });
    }



}
