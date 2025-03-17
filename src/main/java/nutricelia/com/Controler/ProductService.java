package nutricelia.com.Controler;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.NutritionalValue;
import nutricelia.com.Model.Product;
import org.hibernate.ObjectNotFoundException;


@ApplicationScoped
public class ProductService {

    public Uni<Product> findById(int id) {
        return Product.<Product>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "Product"));
    }

    public Uni<NutritionalValue> getNutritionalValue(int id){
        return NutritionalValue.find("product.id", id)
                .firstResult()
                .map(entity -> (NutritionalValue) entity)
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "NutritionalValue"));
    }

}
