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
    public UsersWithAccessToListId id;

    @Column(nullable = false)
    public boolean propietario;
}
