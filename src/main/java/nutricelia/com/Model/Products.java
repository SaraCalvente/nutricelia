package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Producto")
public class Products extends PanacheEntityBase {

    @Id
    @Column(unique = true, nullable = false)
    public int id;
    
    @Column(nullable = false)
    public String nombre;

    @Column(nullable = false)
    public int sinGluten;

    @Column(nullable = false)
    public String marca;

}
