package nutricelia.com.Controler.ListasCompra;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import nutricelia.com.Model.BuyList;
import org.hibernate.ObjectNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BuyListService {
    public Uni<BuyList> findById(int id) {
        return BuyList.<BuyList>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "BuyList"));
    }

    public Uni<BuyList> findByName(String name) {
        return BuyList.find("nombre", name).firstResult();
    }

    public Uni<List<BuyList>> list() {
        return BuyList.listAll();
    }

    @ReactiveTransactional
    public Uni<BuyList> create(BuyList buyList) {
        return buyList.persistAndFlush();
    }

    @ReactiveTransactional
    public Uni<Void> delete(int id) {
        return BuyList.findById(id)
                .onItem().ifNotNull().call(buyList -> buyList.delete())
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "BuyList"))
                .replaceWithVoid();
    }

    @ReactiveTransactional
    public Uni<BuyList> update(int id, BuyList updatedData) {
        return findById(id)
                .invoke(existingList -> {
                    existingList.nombre = updatedData.nombre;
                })
                .call(BuyList::flush); // Guarda los cambios en la base de datos
    }

}