package nutricelia.com.Model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HistoryId implements Serializable {
    public String email;
    public int id_producto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryId)) return false;
        HistoryId that = (HistoryId) o;
        return id_producto == that.id_producto &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, id_producto);
    }
}
