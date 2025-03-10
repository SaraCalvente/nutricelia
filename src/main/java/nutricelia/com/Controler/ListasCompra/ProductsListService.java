package nutricelia.com.Controler.ListasCompra;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.ProductsList;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

@ApplicationScoped
public class ProductsListService {
    public Uni<ProductsList> findById(long id) {
        return ProductsList.<ProductsList>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "ProductList"));
    }

    public Uni<ProductsList> findByName(int id_producto, int id_lista) {
        return ProductsList.find("id_producto = ?1 and id_lista = ?2", id_producto, id_lista).firstResult();
    }

    public Uni<List<ProductsList>> list() {
        return ProductsList.listAll();
    }

    @ReactiveTransactional
    public Uni<ProductsList> create(ProductsList productsList) {
        return productsList.persistAndFlush();
    }

    public Uni<List<ProductsList>> findByListId(int id_lista) {
        return ProductsList.find("id_lista", id_lista).list();
    }
    /*
    @ReactiveTransactional
    public Uni<BuyList> update(BuyList buyList) {
        return findById(buyList.id).chain(s -> s.merge(buyList));
    }
    */
    @ReactiveTransactional
    public Uni<Void> delete(long id) {
        return ProductsList.findById(id)
                .onItem().ifNotNull().call(listedProduct -> listedProduct.delete())
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "listedProduct"))
                .replaceWithVoid();
    }
    /*
    public Uni<BuyList> getCurrentBuyList() {
        // TODO: replace implementation once security is added to the project
        return BuyList.find("order by ID").firstResult();
    }

     */

}