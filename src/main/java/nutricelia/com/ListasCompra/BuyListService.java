package nutricelia.com.ListasCompra;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import org.hibernate.ObjectNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
@ApplicationScoped
public class UserService {
    public Uni<BuyList> findById(long id) {
        return BuyList.<BuyList>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "User"));
    }
}