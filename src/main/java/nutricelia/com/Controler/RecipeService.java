package nutricelia.com.Controler;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import nutricelia.com.Model.Recipe;
import nutricelia.com.Model.RecipeId;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

@ApplicationScoped
public class RecipeService {

    @ReactiveTransactional
    public Uni<List<Recipe>> getByEmail(String email) {
        return Recipe.find("recipeId.email", email).list();
    }

    @ReactiveTransactional
    public Uni<Recipe> getById(RecipeId id) {
        return Recipe.<Recipe>findById(id)
                .onItem().ifNull().failWith(() ->
                        new ObjectNotFoundException(id, "Recipe")
                );
    }

    @ReactiveTransactional
    public Uni<Void> deleteRecipe(RecipeId id) {
        return Recipe.findById(id)
                .chain(recipe -> {
                    if (recipe == null) {
                        return Uni.createFrom().failure(new ObjectNotFoundException(id, "Recipe"));
                    }
                    return recipe.delete()
                            .onItem().ignore().andContinueWithNull();
                });
    }

    @ReactiveTransactional
    public Uni<Recipe> create(Recipe recipe) {
        return recipe.<Recipe>persist()
                .onItem().ifNull().failWith(() -> new RuntimeException("Error al guardar la receta"))
                .onItem().transformToUni(savedRecipe -> Uni.createFrom().item(savedRecipe));
    }
}