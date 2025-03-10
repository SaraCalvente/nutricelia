package nutricelia.com.Controler;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.History;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

@ApplicationScoped
public class HistoryService {
    public Uni<History> findById(long id) {
        return History.<History>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "History"));
    }

    public Uni<History> findByName(int id_producto, String email) {
        return History.find("id_producto = ?1 and email = ?2", id_producto, email).firstResult();
    }

    public Uni<List<History>> list() {
        return History.listAll();
    }

    @ReactiveTransactional
    public Uni<History> create(History history) {
        return history.persistAndFlush();
    }

    public Uni<List<History>> findByUserMail(String email) {
        return History.find("email", email).list();
    }
    /*
    @ReactiveTransactional
    public Uni<BuyList> update(BuyList buyList) {
        return findById(buyList.id).chain(s -> s.merge(buyList));
    }
    */
    @ReactiveTransactional
    public Uni<Void> delete(long id) {
        return History.findById(id)
                .onItem().ifNotNull().call(history -> history.delete())
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "History"))
                .replaceWithVoid();
    }
    /*
    public Uni<BuyList> getCurrentBuyList() {
        // TODO: replace implementation once security is added to the project
        return BuyList.find("order by ID").firstResult();
    }

     */

}
