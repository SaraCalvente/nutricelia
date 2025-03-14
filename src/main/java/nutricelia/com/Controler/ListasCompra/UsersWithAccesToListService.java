package nutricelia.com.Controler.ListasCompra;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import nutricelia.com.Model.BuyList;
import nutricelia.com.Model.ListedProduct;
import nutricelia.com.Model.UsersWithAccesToList;
import org.hibernate.ObjectNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UsersWithAccesToListService {
    public Uni<UsersWithAccesToList> findById(long id) {
        return UsersWithAccesToList.<UsersWithAccesToList>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "UsersWithAccesToList"));
    }

    public Uni<UsersWithAccesToList> findByName(String email, int id_lista) {
        return BuyList.find("email = ?1 and id_lista = ?2", email, id_lista).firstResult();
    }

    public Uni<List<UsersWithAccesToList>> list() {
        return UsersWithAccesToList.listAll();
    }

    @ReactiveTransactional
    public Uni<UsersWithAccesToList> create(UsersWithAccesToList usersWithAccesToList) {
        return usersWithAccesToList.persistAndFlush();
    }
    /*
    @ReactiveTransactional
    public Uni<BuyList> update(BuyList buyList) {
        return findById(buyList.id).chain(s -> s.merge(buyList));
    }

    */
    @ReactiveTransactional
    public Uni<Void> delete(long id) {
        return UsersWithAccesToList.findById(id)
                .onItem().ifNotNull().call(usersWithAccesToList -> usersWithAccesToList.delete())
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