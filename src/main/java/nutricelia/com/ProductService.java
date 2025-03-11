package nutricelia.com;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;

@ApplicationScoped
public class ProductService {

    @RestClient
    OpenFoodFactsClient openFoodFactsClient;


    public ProductSearchResponse searchProductsByStore(String store) {
        String fields = "product_name,brands_tags,allergens_tags,categories_tags,nutriments";
        ProductSearchResponse listaProductos = new ProductSearchResponse();

        for (int i = 0; i < 100; i++) {
            ProductSearchResponse response = openFoodFactsClient.searchProductsByStore(store, fields, 100, i);

            if (response != null && response.getProducts() != null) {
                listaProductos.getProducts().addAll(response.getProducts());
            }

        }
        return listaProductos;

    }

    public ProductSearchResponse searchProductsByStore1(String store) {
        String fields = "product_name,brands_tags,allergens_tags,categories_tags,nutriments";
        ProductSearchResponse listaProductos = new ProductSearchResponse();

        // Asegurar que la lista de productos no sea null
        if (listaProductos.getProducts() == null) {
            listaProductos.setProducts(new ArrayList<>());
        }

        for (int i = 0; i < 56; i++) {
            ProductSearchResponse response = openFoodFactsClient.searchProductsByStore(store, fields, 100, i);

            if (response != null && response.getProducts() != null) {
                listaProductos.getProducts().addAll(response.getProducts());  // Ahora no habrá NullPointerException
            }
        }

        return listaProductos;
    }


}