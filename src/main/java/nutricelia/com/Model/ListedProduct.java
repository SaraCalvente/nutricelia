package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "ProductosLista")
public class ListedProduct extends PanacheEntity {
    @Column(nullable = false)
    public int id_lista;

    @Column(nullable = false)
    public int id_producto;

}