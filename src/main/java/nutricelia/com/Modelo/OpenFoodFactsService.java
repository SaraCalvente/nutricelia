package nutricelia.com.Modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.codehaus.jackson.map.ObjectMapper;

public class OpenFoodFactsService {

    private static final String API_URL = "https://world.openfoodfacts.org/api/v2/search?purchase_places_tags=mercadona&fields=product_name,nutriments,purchase_places_tags,brands_tags,allergens_tags";

    public void obtenerDatosDeProductos() {
        try {
            // Crear una URL con la consulta
            URL url = new URL(API_URL);

            // Establecer la conexión
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
            // Aquí puedes convertir el JSON a objetos si es necesario

            // Imprimir la respuesta (o procesarla como prefieras)
            System.out.println(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

