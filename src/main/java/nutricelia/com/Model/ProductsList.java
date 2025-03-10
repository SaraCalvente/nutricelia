package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProductosLista")
public class ProductsList extends PanacheEntity {
    @EmbeddedId
    public ProductsListId productsListId;
}