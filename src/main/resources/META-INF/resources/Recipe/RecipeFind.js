    document.addEventListener('DOMContentLoaded', function() {

        document.getElementById('recipeSearchForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const ingredient = document.getElementById('ingredientInput').value.trim();
            const healthOptions = [];

            if (document.getElementById('glutenFree').checked) {
                healthOptions.push('gluten-free');
            }
            if (document.getElementById('dairyFree').checked) {
                healthOptions.push('dairy-free');
            }

            if (ingredient) {
                searchRecipes(ingredient, healthOptions);
            }
        });
    });

    function searchRecipes(query, healthOptions) {
        const resultsSection = document.getElementById('resultsSection');
        const loadingElement = document.getElementById('loading');

        // Mostrar carga
        resultsSection.innerHTML = '';
        loadingElement.style.display = 'block';

        // Construir URL de la API
        let apiUrl = `https://api.edamam.com/api/recipes/v2?type=public&q=${encodeURIComponent(query)}&app_id=0503d2e6&app_key=9e13739c1d1f906638cfda450be0648c`;

        // Añadir opciones de salud si existen
        if (healthOptions.length > 0) {
            apiUrl += `&health=${healthOptions.join('&health=')}`;
        }

        // Realizar la petición
        fetch(apiUrl, {
            headers: {
                'accept': 'application/json',
                'Edamam-Account-User': 'Edamam-Acount-User',
                'Accept-Language': 'en'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta de la API');
            }
            return response.json();
        })
        .then(data => {
            loadingElement.style.display = 'none';
            displayRecipes(data.hits);
        })
        .catch(error => {
            loadingElement.style.display = 'none';
            resultsSection.innerHTML = `<p style="color: red; text-align: center;">Error al buscar recetas: ${error.message}</p>`;
            console.error('Error:', error);
        });
    }

   function displayRecipes(recipes) {
       const resultsSection = document.getElementById('resultsSection');

       if (!recipes || recipes.length === 0) {
           resultsSection.innerHTML = '<p style="text-align: center;">No se encontraron recetas con esos ingredientes.</p>';
           return;
       }

       let html = '';

       recipes.forEach(recipeData => {
           const recipe = recipeData.recipe;

           html += `
               <div class="recipe-card">
                   <img src="${recipe.image}" alt="${recipe.label}" class="recipe-image">
                   <div class="recipe-info">
                       <h3 class="recipe-title">${recipe.label}</h3>
                       <p><strong>Calorías:</strong> ${Math.round(recipe.calories)} kcal</p>
                       <p><strong>Tiempo:</strong> ${recipe.totalTime || 'No especificado'} min</p>
                       <p class="recipe-ingredients">
                           <strong>Ingredientes principales:</strong>
                           ${recipe.ingredientLines.slice(0, 3).join(', ')}
                       </p>
                       <a href="${recipe.url}" target="_blank" style="color: #4CAF50; text-decoration: none;">Ver receta completa</a>
                       <button class="save-recipe-btn"
                               data-name="${encodeURIComponent(recipe.label)}"
                               data-ingredients='${JSON.stringify(recipe.ingredientLines)}'
                               data-instructions="${encodeURIComponent(recipe.url)}">
                           Guardar Receta
                       </button>
                   </div>
               </div>
           `;
       });

       resultsSection.innerHTML = html;

       // Añadir event listeners a los botones
       document.querySelectorAll('.save-recipe-btn').forEach(button => {
           button.addEventListener('click', function() {
               const recipeName = decodeURIComponent(this.dataset.name);
               const ingredients = JSON.parse(this.dataset.ingredients);
               const instructions = decodeURIComponent(this.dataset.instructions);

               saveRecipe(recipeName, ingredients, instructions);
           });
       });
    }

function saveRecipe(nombre, ingredientes, instrucciones) {
    const recipeData = {
        recipeId: {
            nombre: nombre
        },
        ingredientes: ingredientes.join('\n'),
        instrucciones: instrucciones
    };

    fetch('/recipe', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // Si usas JWT
        },
        credentials: 'include', // Necesario si usas cookies de sesión
        body: JSON.stringify(recipeData)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text) });
        }
        return response.json();
    })
    .then(() => alert('Receta guardada correctamente'))
    .catch(error => {
        console.error('Error:', error);
        alert('Error al guardar la receta: ' + error.message);
    });
}
