package nutricelia.com.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "ListaCompra")
public class BuyList extends PanacheEntityBase {
    @Column(nullable = false)
    public String nombre;

    @Id
    @Column(unique = true, nullable = false)
    public long id;

    @JsonProperty("nombre")
    public void setName(String nombre) {
        this.nombre = nombre;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // No devolver el password en las respuestas JSON
    public String getName() {
        return nombre;
    }


}
