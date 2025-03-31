package nutricelia.com.Model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RecipeId implements Serializable {
    public String nombre;
    public String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeId)) return false;
        RecipeId that = (RecipeId) o;
        return nombre.equals(that.nombre)&&
                email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, email);
    }
}

