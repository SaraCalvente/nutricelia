package nutricelia.com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class OpenFoodFactsClient {

    private static final String API_URL = "https://world.openfoodfacts.org/api/v2";


    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Para convertir objetos a JSON


    public OpenFoodFactsClient(@Client(API_URL) HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Product> fetchProductsFromMercadona() {
        String url = API_URL + "/search?stores_tags=mercadona&fields=product_name,brands_tags,categories_tags";
        System.out.println("URL de la solicitud: " + url); // Imprime la URL


        System.out.println("URL de la solicitud: " + url); // Imprime la URL

        HttpRequest<?> request = HttpRequest.GET(url);
        System.out.println("Solicitud HTTP creada: " + request); // Imprime la solicitud

        HttpResponse<ProductResponse> response = httpClient.toBlocking().exchange(request, ProductResponse.class);
        System.out.println("Respuesta recibida: " + response.getStatus()); // Imprime el estado de la respuesta

        if (response.getStatus().getCode() == 200) {
            ProductResponse productResponse = response.body();
            try {
                // Convierte el cuerpo de la respuesta a JSON y lo imprime
                String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(productResponse);
                System.out.println("Cuerpo de la respuesta (JSON):\n" + jsonResponse);
            } catch (Exception e) {
                System.out.println("Error al convertir la respuesta a JSON: " + e.getMessage());
            }
            return productResponse.getProducts();
        } else {
            System.out.println("Error en la respuesta: " + response.getStatus()); // Imprime el error
            throw new RuntimeException("Error fetching products from Mercadona");
        }
    }
}