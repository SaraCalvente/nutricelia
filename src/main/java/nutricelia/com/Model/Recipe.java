package nutricelia.com.Model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name="Receta")
public class
Recipe extends PanacheEntityBase {

    @EmbeddedId
    private RecipeId recipeId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String ingredientes;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instrucciones;

    public RecipeId getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(RecipeId recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingrediente) {
        this.ingredientes = ingrediente;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instruccion) {
        this.instrucciones = instruccion;
    }
}
