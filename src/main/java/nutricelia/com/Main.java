package nutricelia.com;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;

public class Main {

    public static void main(String[] args) {
        // Arrancar Micronaut y obtener el contexto de la aplicación
        ApplicationContext context = Micronaut.build(args).start();

        // Obtener el servicio ProductService desde el contexto
        ProductService productService = context.getBean(ProductService.class);

        // Llamar al método para obtener e imprimir los productos
        productService.fetchAndSaveMercadonaProducts();

        // Detener el contexto de la aplicación cuando termine
        context.close();
    }
}