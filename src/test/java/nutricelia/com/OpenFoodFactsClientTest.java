package nutricelia.com;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class OpenFoodFactsClientTest {

    @Inject
    @Client("https://world.openfoodfacts.org") // Cliente HTTP real
    private HttpClient httpClient;

    private OpenFoodFactsClient openFoodFactsClient;

    @Test
    public void testFetchProductsFromMercadona_Success() {
        // Crea una instancia real de OpenFoodFactsClient con el cliente HTTP real
        openFoodFactsClient = new OpenFoodFactsClient(httpClient);

        // Llama al método real que interactúa con la API
        List<Product> products = openFoodFactsClient.fetchProductsFromMercadona();

        // Verifica que se obtuvieron productos
        assertNotNull(products);
        assertFalse(products.isEmpty()); // Verifica que la lista no esté vacía

        // Imprime los productos para verlos en la consola
        products.forEach(product -> System.out.println("Product: " + product.getProductName()));
    }

    @Test
    public void testFetchProductsFromMercadona_Failure() {
        // Crea una instancia real de OpenFoodFactsClient con el cliente HTTP real
        openFoodFactsClient = new OpenFoodFactsClient(httpClient);

        // Simula un error en la URL (por ejemplo, una URL incorrecta)
        assertThrows(RuntimeException.class, () -> {
            openFoodFactsClient.fetchProductsFromMercadona();
        });
    }
}