package nutricelia.com.Controler.ListasCompra;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import nutricelia.com.Model.UsersWithAccessToList;
import nutricelia.com.Model.UsersWithAccessToListId;
import org.hibernate.ObjectNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UsersWithAccessToListService {

    public Uni<UsersWithAccessToList> findById(UsersWithAccessToListId id) {
        return UsersWithAccessToList.<UsersWithAccessToList>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "UsersWithAccesToList"));
    }

    public Uni<List<UsersWithAccessToList>> findByUserEmail(String email) {
        return UsersWithAccessToList.find("usersWithAccessToListId.email", email).list();
    }

    public Uni<List<UsersWithAccessToList>> findByListId(int id_lista) {
        return UsersWithAccessToList.find("usersWithAccessToListId.id_lista", id_lista).list();
    }

    public Uni<List<UsersWithAccessToList>> list() {
        return UsersWithAccessToList.listAll();
    }

    @ReactiveTransactional
    public Uni<UsersWithAccessToList> create(UsersWithAccessToList usersWithAccessToList) {
        return usersWithAccessToList.persistAndFlush();
    }

    @ReactiveTransactional
    public Uni<Void> delete(UsersWithAccessToListId id) {
        return findById(id)
                .onItem().ifNotNull().call(PanacheEntityBase::delete)
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "usersWithAccessToListId"))
                .replaceWithVoid();
    }
}