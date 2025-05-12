package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "foro")
public class Forum extends PanacheEntityBase {

    @Id
    @Column(nullable = false, length = 20)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private User usuario;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
