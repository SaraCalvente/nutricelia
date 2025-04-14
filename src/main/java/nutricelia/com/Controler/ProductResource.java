package nutricelia.com.Controler;


import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import io.quarkus.qute.Template;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nutricelia.com.Model.NutritionalValue;
import nutricelia.com.Model.Product;


@Path("/product")

public class ProductResource {

    private final ProductService productService;

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Inject
    Template productView;

    @Inject
    Template similarProductsView;
    @Inject
    Template compareProductsView;


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
    @NonBlocking
    public Uni<Response> getProduct(@PathParam("id") int id) {
        return productService.getNutritionalValue(id)
                .onItem().transformToUni(nutritionalValue -> {
                    if (nutritionalValue == null) {
                        return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND)
                                .entity("Producto no encontrado").build());
                    }
                    return productService.similarProducts(id)
                            .onItem().transform(similarProducts -> {
                                String renderHtml = similarProductsView
                                        .data("nutritionalValue", nutritionalValue)
                                        .data("product", nutritionalValue.product)
                                        .data("similarProducts", similarProducts)
                                        .render();
                                return Response.ok(renderHtml).build();
                            });
                });
    }

    @GET
    @Path("/{id}/{id2}")
    @Produces(MediaType.TEXT_HTML)
    @NonBlocking
    public Uni<Response> compareProducts(@PathParam("id") int id, @PathParam("id2") int id2) {
        return productService.getNutritionalValue(id)
                .onItem().transformToUni(nv1 -> {
                    if (nv1 == null) {
                        return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND)
                                .entity("Producto con ID " + id + " no encontrado").build());
                    }
                    return productService.getNutritionalValue(id2)
                            .onItem().transformToUni(nv2 -> {
                                if (nv2 == null) {
                                    return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND)
                                            .entity("Producto con ID " + id2 + " no encontrado").build());
                                }

                                return productService.similarProducts(id)
                                        .onItem().transform(similarProducts -> {
                                            String renderedHtml = compareProductsView
                                                    .data("nutritionalValue1", nv1)
                                                    .data("product1", nv1.product)
                                                    .data("nutritionalValue2", nv2)
                                                    .data("product2", nv2.product)
                                                    .data("similarProducts", similarProducts)
                                                    .render();
                                            return Response.ok(renderedHtml).build();
                                        });
                            });
                });
    }

    @GET
    @Path("/name/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Product> getProductNameById(@PathParam("id") int id) {
        return productService.getProductById(id);
    }
}
