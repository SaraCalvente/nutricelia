package nutricelia.com;

import nutricelia.com.OpenFoodFactsClient;
import nutricelia.com.OpenFoodFactsResponse;
import nutricelia.com.Product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    @RestClient
    OpenFoodFactsClient openFoodFactsClient;

    public List<Product> fetchProducts(String category, String brand) {
        String fields = "product_name,brands,categories";
        OpenFoodFactsResponse response = openFoodFactsClient.searchProducts(category, brand, fields);
        return response.getProducts();
    }
}