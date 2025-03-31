package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name="Receta")
public class Recipe extends PanacheEntityBase {

    @EmbeddedId
    private RecipeId recipeId;

    @Column(nullable = false)
    private String ingrediente;

    @Column(nullable = false)
    private String instruccion;

    public RecipeId getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(RecipeId recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }
}
