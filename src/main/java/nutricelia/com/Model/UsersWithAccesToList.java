package nutricelia.com.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "UsuariosAccesoLista")
public class UsersWithAccesToList extends PanacheEntity {
    @Column(nullable = false)
    boolean propietari;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    int id_lista;

    @JsonProperty("propietari")
    public void setOwner(boolean propietari) {
        this.propietari = propietari;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // No devolver el password en las respuestas JSON
    public boolean getOwner() {
        return propietari;
    }
}
