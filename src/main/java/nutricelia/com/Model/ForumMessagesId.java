package nutricelia.com.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class ForumMessagesId implements Serializable {

    @Column(name = "fecha_publicacion")  // <- Aquí está la clave
    private LocalDateTime fechaPublicacion;

    @Column(name = "nombre_foro")
    private String nombreForo;

    @Column(name = "email")
    private String email;
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getNombreForo() {
        return nombreForo;
    }

    public void setNombreForo(String nombreForo) {
        this.nombreForo = nombreForo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // equals y hashCode (muy importantes)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForumMessagesId)) return false;
        ForumMessagesId that = (ForumMessagesId) o;
        return Objects.equals(fechaPublicacion, that.fechaPublicacion) &&
                Objects.equals(nombreForo, that.nombreForo) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaPublicacion, nombreForo, email);
    }
}