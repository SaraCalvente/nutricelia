package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ValorNutricional")
public class NutritionalValue extends PanacheEntityBase {

    @Column(unique = true, nullable = false)
    @Id
    private double id;

    @Column
    private double calorias;

    @Column
    private double proteina;

    @Column
    private double grasas;

    @Column
    private double azucar;

    @Column
    private double carbohidratos;

    @Column
    private double sal;
}
