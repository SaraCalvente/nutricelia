package nutricelia.com.Controler.ListasCompra;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.ListedProduct;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

@ApplicationScoped
public class ListedProductService {
    public Uni<ListedProduct> findById(long id) {
        return ListedProduct.<ListedProduct>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "ProductList"));
    }

    public Uni<ListedProduct> findByName(int id_producto, int id_lista) {
        return ListedProduct.find("id_producto = ?1 and id_lista = ?2", id_producto, id_lista).firstResult();
    }

    public Uni<List<ListedProduct>> list() {
        return ListedProduct.listAll();
    }

    @ReactiveTransactional
    public Uni<ListedProduct> create(ListedProduct listedProduct) {
        return listedProduct.persistAndFlush();
    }
    /*
    @ReactiveTransactional
    public Uni<BuyList> update(BuyList buyList) {
        return findById(buyList.id).chain(s -> s.merge(buyList));
    }

    @ReactiveTransactional
    public Uni<Void> delete(long id) {
        return findById(id)
                .chain(u -> Uni.combine().all().unis(
                                        Task.delete("BuyList.id", u.id),
                                        Project.delete("BuyList.id", u.id)
                                ).asTuple()
                                .chain(t -> u.delete())
                );

    public Uni<BuyList> getCurrentBuyList() {
        // TODO: replace implementation once security is added to the project
        return BuyList.find("order by ID").firstResult();
    }

     */

}