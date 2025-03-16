package nutricelia.com.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuario")
public class User extends PanacheEntityBase {

    @Column(nullable = false)
    public String nombre;

    @Id
    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = false)
    String pwd;

    @JsonProperty("password")
    public void setPassword(String password) {
        this.pwd = password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // No devolver el password en las respuestas JSON
    public String getPassword() {
        return pwd;
    }

}
