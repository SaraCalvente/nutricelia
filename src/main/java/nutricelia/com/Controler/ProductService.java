package nutricelia.com.Controler;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.NutritionalValue;
import nutricelia.com.Model.Product;
import org.hibernate.ObjectNotFoundException;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@ApplicationScoped
public class ProductService {


    public Uni<Product> findById(int id) {
        return Product.<Product>findById(id)
                .onItem().ifNull().failWith(() -> new
                        ObjectNotFoundException(id, "Product"));
    }

    public Uni<NutritionalValue> getNutritionalValue(int id) {
        return NutritionalValue.find("product.id", id)
                .firstResult();
    }


    public Uni<Product> getProductById(int id) {
        return Product.findById(id);
    }


    public Uni<List<NutritionalValue>> similarProducts(int id) {
        return getProductById(id)
                .onItem().transformToUni(product -> {
                    String category = product.getCategoria();
                    if(category.isEmpty() || category.isBlank()){
                        return Uni.createFrom().item(List.of());                    }
                    return Product.<Product>find("categoria like ?1", "%" + category + "%").list()
                            .onItem().transformToUni(sameCategory -> {
                                List<Uni<NutritionalValue>> unis = new ArrayList<>();

                                return getNutritionalValue(id)
                                        .onItem().transformToUni(mainNutritionalValue -> {
                                            for (Product currentProduct : sameCategory) {
                                                if (currentProduct.getId() != product.getId()) {
                                                    Uni<NutritionalValue> uni = getNutritionalValue(currentProduct.getId())
                                                            .onItem().transform(currentNutritionalValue -> {
                                                                if (currentNutritionalValue != null &&
                                                                        sonSimilares(mainNutritionalValue, currentNutritionalValue, 0.1)) {
                                                                    return currentNutritionalValue;
                                                                }
                                                                return null;
                                                            })
                                                            .onFailure().recoverWithItem(() -> null);

                                                    unis.add(uni);
                                                }
                                            }
                                            return Uni.join().all(unis)
                                                    .andCollectFailures()
                                                    .onItem().transform(list ->
                                                            list.stream()
                                                                    .filter(Objects::nonNull)
                                                                    .collect(Collectors.toList()));                                        });
                            });
                });
    }


    private static boolean sonSimilares(NutritionalValue nut1, NutritionalValue nut2, double porcentaje) {
        double distancia = euclideanDistance(nut1, nut2);
        double referencia = Math.max(nut1.getCalorias(), nut2.getCalorias());
        return (distancia / referencia) <= porcentaje;
    }

    private static double euclideanDistance(NutritionalValue nut1, NutritionalValue nut2) {
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