package nutricelia.com;

import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import javax.persistence.EntityManager;
import javax.inject.Inject;
import java.util.List;

@Singleton
public class ProductService {

    private final OpenFoodFactsClient openFoodFactsClient;
    private final EntityManager entityManager;

    @Inject
    public ProductService(OpenFoodFactsClient openFoodFactsClient, EntityManager entityManager) {
        this.openFoodFactsClient = openFoodFactsClient;
        this.entityManager = entityManager;
    }

    @Transactional
    public void fetchAndSaveMercadonaProducts() {
        // Obtener los productos de Mercadona desde la API
        List<Product> products = openFoodFactsClient.fetchProductsFromMercadona();

        // Verificar la respuesta de la API (imprimir productos)
        for (Product product : products) {
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Brands: " + product.getBrandsTags());
            System.out.println("Categories: " + product.getCategoriesTags());
        }

        // Guardar los productos en la base de datos
        for (Product product : products) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductName(product.getProductName());
            productEntity.setBrandsTags(product.getBrandsTags());
            productEntity.setCategoriesTags(product.getCategoriesTags());

            entityManager.persist(productEntity);  // Guardar en la base de datos
        }
    }
}