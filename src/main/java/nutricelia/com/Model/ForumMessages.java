package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajeforo")
public class ForumMessages extends PanacheEntityBase {

    @EmbeddedId
    private ForumMessagesId id;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false, insertable = false, updatable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "nombre_foro", nullable = false, insertable = false, updatable = false)
    private Forum foro;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    public ForumMessagesId getId() {
        return id;
    }

    public void setId(ForumMessagesId id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Forum getForo() {
        return foro;
    }

    public void setForo(Forum foro) {
        this.foro = foro;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaPublicacion() {
        return id.getFechaPublicacion();
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.id.setFechaPublicacion(fechaPublicacion);
    }

    public String getEmail() {
        return id.getEmail();
    }

    public void setEmail(String email) {
        this.id.setEmail(email);
    }

    public String getNombreForo() {
        return id.getNombreForo();
    }

    public void setNombreForo(String nombreForo) {
        this.id.setNombreForo(nombreForo);
    }
}