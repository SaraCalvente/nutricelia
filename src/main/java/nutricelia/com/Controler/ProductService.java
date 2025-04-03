package nutricelia.com.Controler;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.NutritionalValue;
import nutricelia.com.Model.Product;
import org.hibernate.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class ProductService {

    public Uni<Product> findById(int id) {
        return Product.<Product>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "Product"));
    }

    public Uni<NutritionalValue> getNutritionalValue(int id){
        return NutritionalValue.find("product.id", id)
                .firstResult()
                .map(entity -> (NutritionalValue) entity)
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "NutritionalValue"));
    }


    public Uni<Product> getProductById(int id){
        return Product.find("id", id)
                .firstResult()
                .map(entity -> (Product) entity)
                .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "Product"));    }


    public Uni<List<NutritionalValue>> similarProducts(int id){
        List<NutritionalValue> result = new ArrayList<>();
        Product product = castUni(getProductById(id));
        String category = product.getCategoria();
        List<Product> sameCategory = castUni(Product.find("categoria", category).list());
        NutritionalValue nutritionalValue = castUni(getNutritionalValue(id));
        for (Product currentProduct : sameCategory) {
            if (currentProduct.getId() != product.getId()) {
                NutritionalValue currentNutritionalValue = (NutritionalValue) getNutritionalValue(currentProduct.getId());
                if (sonSimilares(nutritionalValue, currentNutritionalValue, 0.1)) {
                    result.add(currentNutritionalValue);
                }
            }
        }
        return toUni(result);
    }


    private static boolean sonSimilares(NutritionalValue nut1, NutritionalValue nut2, double porcentaje) {
        double distancia = euclideanDistance(nut1, nut2);
        double referencia = Math.max(nut1.getCalorias(), nut2.getCalorias());

        return (distancia / referencia) <= porcentaje;
    }
    private static double euclideanDistance(NutritionalValue nut1, NutritionalValue nut2){
        return Math.sqrt(
                Math.pow(nut1.getCalorias() - nut2.getCalorias(), 2) +
                        Math.pow(nut1.getProteinas() - nut2.getProteinas(), 2) +
                        Math.pow(nut1.getGrasas() - nut2.getGrasas(), 2) +
                        Math.pow(nut1.getAzucar() - nut2.getAzucar(), 2) +
                        Math.pow(nut1.getCarbohidratos() - nut2.getCarbohidratos(), 2) +
                        Math.pow(nut1.getSal() - nut2.getSal(), 2)
        );
    }


    private static <T> T castUni(Uni<T> uniObject) {
        return uniObject.await().indefinitely();
    }

    private static <T> Uni<T> toUni(T object) {
        return Uni.createFrom().item(object);
    }
}