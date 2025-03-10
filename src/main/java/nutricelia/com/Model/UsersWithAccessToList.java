package nutricelia.com.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "UsuariosAccesoLista")
public class UsersWithAccessToList extends PanacheEntityBase {

    @EmbeddedId
    public UsersWithAccessToListId usersWithAccessToListId;

    @Column(nullable = false)
    public boolean propietario;

    @JsonProperty("propietari")
    public void setOwner(boolean propietario) {
        this.propietario = propietario;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // No devolver el password en las respuestas JSON
    public boolean getOwner() {
        return propietario;
    }
}
