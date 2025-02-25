package nutricelia.com;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Singleton
public class OpenFoodFactsClient {

    private static final String API_URL = "https://world.openfoodfacts.org/api/v2/search";

    public List<Product> fetchProductsFromMercadona() {
        String url = API_URL + "?purchase_places_tags=mercadona&fields=product_name,brands_tags,categories_tags";
        try {
            // Crear una URL con la consulta
            URL requestUrl = new URL(url);

            // Establecer la conexión
            HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            // Leer la respuesta
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Procesar la respuesta en formato JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = response.toString();

            // Mapear la respuesta JSON a un objeto ProductResource (ajustar según la estructura de tu modelo)
            ProductResource productResponse = objectMapper.readValue(jsonResponse, ProductResource.class);

            // Devolver la lista de productos
            return productResponse.getProduct("");  // Ajustar el método según tu clase ProductResource

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching products from Mercadona", e);
        }
    }
}