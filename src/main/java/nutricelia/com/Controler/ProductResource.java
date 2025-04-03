package nutricelia.com.Controler;


import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
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
                        String renderedHtml = productView
                                .data("nutritionalValue", nutritionalValue)
                                .data("product", nutritionalValue.product)
                                .render();
                        return Uni.createFrom().item(Response.ok(renderedHtml).build());
                    }
                    return Uni.createFrom().failure(new NotFoundException("Product not found"));
                });
    }

    @GET
    @Path("/similares/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public Uni<Response> getProduct(@PathParam("id") int id) {
        return productService.getNutritionalValue(id)
                .onItem().transformToUni(nutritionalValue -> {
                    if (nutritionalValue == null) {
                        return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND)
                                .entity("Producto no encontrado").build());
                    }
                    return productService.similarProducts(id)
                            .onItem().transform(similarProducts -> {
                                String renderHtml = productView
                                        .data("nutritionalValue", nutritionalValue)
                                        .data("product", nutritionalValue.product)
                                        .data("similarProducts", similarProducts)
                                        .render();
                                return Response.ok(renderHtml).build();
                            });
                });
    }
}
