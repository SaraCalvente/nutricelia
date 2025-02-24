package nutricelia.com.ListasCompra;
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
}
