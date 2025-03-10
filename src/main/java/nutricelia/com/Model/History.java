package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "HistorialUsuario")
public class History extends PanacheEntity {
    @EmbeddedId
    public HistoryId historyId;

}