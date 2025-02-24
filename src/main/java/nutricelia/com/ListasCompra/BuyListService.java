package nutricelia.com.ListasCompra;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import org.hibernate.ObjectNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BuyListService {
    public Uni<BuyList> findById(long id) {
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