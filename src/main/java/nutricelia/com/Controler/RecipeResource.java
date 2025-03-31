package nutricelia.com.Controler;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import nutricelia.com.Model.Recipe;
import nutricelia.com.Model.RecipeId;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.util.List;

@Path("/recipe")
public class RecipeResource {
    private final RecipeService recipeService;

    @Inject
    public RecipeResource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Recipe>> get(@Context SecurityContext securityContext) {
        String email = securityContext.getUserPrincipal().getName(); // Obtiene el email del usuario autenticado
        return recipeService.getByEmail(email);
    }

    @GET
    @Path("/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Recipe> getRecipe(@PathParam("nombre") String nombre, @Context SecurityContext securityContext) {
        String email = securityContext.getUserPrincipal().getName(); // Obtiene el email del usuario autenticado
        RecipeId id = new RecipeId();
        id.nombre = nombre;
        id.email = email;
        return recipeService.getById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Receta no encontrada", 404));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<Recipe> create(Recipe recipe, @Context SecurityContext securityContext) {
        recipe.getRecipeId().email = securityContext.getUserPrincipal().getName(); // Asigna el email al RecipeId
        return recipeService.create(recipe);
    }

    @DELETE
    @Path("/delete/{nombre}")
    public Uni<Void> delete(@PathParam("nombre") String nombre, @Context SecurityContext securityContext) {
        String email = securityContext.getUserPrincipal().getName(); // Obtiene el email del usuario autenticado
        RecipeId id = new RecipeId();
        id.nombre = nombre;
        id.email = email;
        return recipeService.deleteRecipe(id);
    }
}
