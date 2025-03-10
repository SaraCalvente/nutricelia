package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProductosLista")
public class ProductsList extends PanacheEntityBase {
    @EmbeddedId
    public ProductsListId productsListId;
}