package nutricelia.com.OpenFoodFactsAPI;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.client.annotation.Client;
import pl.coderion.model.ProductResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import javax.inject.Singleton;

@Singleton
public class OpenFoodFactsClient {
    private static final String API_URL = "https://world.openfoodfacts.org/api/v2";

    private final HttpClient httpClient;

    public OpenFoodFactsClient(@Client HttpClient httpClient) throws Exception {
        this.httpClient = httpClient;
    }

    public ProductResponse fetchProductByCode(String code) {
        HttpRequest<?> request =
                HttpRequest.GET(String.format("%s/product/%s.json", API_URL, code))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_JSON);
        ProductResponse response = httpClient.toBlocking().retrieve(request, ProductResponse.class);
        return response;
    }

    public ProductResponse fetchMercadonaProductsByCategory (String category){
        HttpRequest<?> request =
                HttpRequest.GET(String.format("%s/search?" +
                                "stores_tags=mercadona&" +
                                "categories_tags=bread&" +
                                "sort_by=nutriscore_score&" +
                                "fields=product_name,nutriments,brands_tags,allergens_tags", API_URL, category))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        return httpClient.toBlocking().retrieve(request, ProductResponse.class);

    }
}
