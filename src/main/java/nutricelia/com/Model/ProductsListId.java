package nutricelia.com.Model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductsListId implements Serializable {
    public int id_lista;
    public int id_producto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductsListId)) return false;
        ProductsListId that = (ProductsListId) o;
        return id_lista == that.id_lista &&
                id_producto == that.id_producto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_producto, id_lista);
    }
}
