package nutricelia.com.OpenFoodFactsAPI;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/productos")
public class OpenFoodFactsController {

    @Inject
    OpenFoodFactsClient openFoodFactsClient;

    @Get("/buscar/{codigo}")
    public Product buscarPorCodigo(String codigo) {
        return openFoodFactsClient.fetchProductByCode(codigo);
    }

    @Get("/mercadona/{categoria}")
    public SearchResponse buscarProductosMercadona(String categoria) {
        return openFoodFactsClient.fetchMercadonaProductsByCategory(categoria);
    }
}