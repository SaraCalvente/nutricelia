package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "ValorNutricional")
public class NutritionalValue extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)  // Define la relación
    public Product product;
    @Column(name = "calorias_100g")
    private double calorias;

    @Column(name = "proteinas_100g")
    private double proteinas;

    @Column(name = "grasas_100g")
    private double grasas;

    @Column(name = "azucar_100g")
    private double azucar;

    @Column(name = "carbohidratos_100g")
    private double carbohidratos;

    @Column(name = "sal_100g")
    private double sal;



    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {

        this.product = product;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteina) {
        this.proteinas = proteina;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    public double getAzucar() {
        return azucar;
    }
    public void setAzucar(double azucar) {
        this.azucar = azucar;
    }
    public double getCarbohidratos() {
        return carbohidratos;
    }
    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }
    public double getSal() {
        return sal;
    }
    public void setSal(double sal) {
        this.sal = sal;
    }

}
