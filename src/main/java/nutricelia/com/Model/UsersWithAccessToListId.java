package nutricelia.com.Model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsersWithAccessToListId implements Serializable {
    public String email;
    public int id_lista;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsersWithAccessToListId)) return false;
        UsersWithAccessToListId that = (UsersWithAccessToListId) o;
        return id_lista == that.id_lista &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, id_lista);
    }
}
