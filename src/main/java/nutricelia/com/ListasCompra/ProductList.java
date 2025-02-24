package nutricelia.com.ListasCompra;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "ProductosLista")
public class ProductList extends PanacheEntity {
    @Column(nullable = false)
    int id_lista;

    @Column(nullable = false)
    int id_producto;

}