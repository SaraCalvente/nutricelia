package nutricelia.com.Controler.ListasCompra;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.ProductsList;
import nutricelia.com.Model.ProductsListId;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

@ApplicationScoped
public class ProductsListService {
    public Uni<ProductsList> findById(ProductsListId id) {
        return ProductsList.<ProductsList>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "ProductList"));
    }

    public Uni<List<ProductsList>> list() {
        return ProductsList.listAll();
    }

    @ReactiveTransactional
    public Uni<ProductsList> create(ProductsList productsList) {
        return productsList.persistAndFlush();
    }

    public Uni<List<ProductsList>> findByListId(int id_lista) {
        return ProductsList.find("productsListId.id_lista", id_lista).list();
    }

    @ReactiveTransactional
    public Uni<Void> delete(ProductsListId id) {
        return findById(id)
                .onItem().ifNotNull().call(PanacheEntityBase::delete)
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "productsList"))
                .replaceWithVoid();
    }
}