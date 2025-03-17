package nutricelia.com.Controler;


import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import io.quarkus.qute.Template;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.annotation.XmlTransient;


@Path("/product")

public class ProductResource {

    private final ProductService productService;

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Inject
    Template productView;


    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Uni<Response> getProductById(@PathParam("id") int id) {
        return productService.getNutritionalValue(id)
                .onItem().transformToUni(nutritionalValue -> {
                    if (nutritionalValue != null) {
                        // Renderizamos la plantilla con los datos necesarios
                        String renderedHtml = productView
                                .data("nutritionalValue", nutritionalValue)
                                .data("product", nutritionalValue.product)
                                .render(); // Renderizamos la plantilla a HTML
                        // Devolvemos una respuesta con el HTML generado
                        return Uni.createFrom().item(Response.ok(renderedHtml).build());
                    }
                    return Uni.createFrom().failure(new NotFoundException("Product not found"));
                });
    }
}
