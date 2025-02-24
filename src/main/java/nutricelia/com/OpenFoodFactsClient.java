package nutricelia.com;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

import java.util.List;

@Singleton
public class OpenFoodFactsClient {

    private static final String API_URL = "https://world.openfoodfacts.org/api/v2";

    private final HttpClient httpClient;

    public OpenFoodFactsClient(@Client(API_URL) HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Product> fetchProductsFromMercadona() {
        String url = API_URL + "/search?stores_tags=mercadona&fields=product_name,brands_tags,categories_tags";

        HttpRequest<?> request = HttpRequest.GET(url);
        HttpResponse<ProductResponse> response = httpClient.toBlocking().exchange(request, ProductResponse.class);

        if (response.status().getCode() == 200) {
            ProductResponse productResponse = response.body();
            return productResponse.getProducts();
        } else {
            throw new RuntimeException("Error fetching products from Mercadona");
        }
    }
}