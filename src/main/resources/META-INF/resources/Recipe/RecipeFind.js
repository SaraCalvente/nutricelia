    document.addEventListener('DOMContentLoaded', function() {
        // Cargar historial al iniciar la página
        loadUserHistory('usuario@ejemplo.com'); // Cambiar por el email real del usuario

        // Manejar el formulario de búsqueda
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
                        <a href="${recipe.url}" target="_blank" style="color: #4CAF50; text-decoration: none;">Ver receta completa →</a>
                    </div>
                </div>
            `;
        });

        resultsSection.innerHTML = html;
    }

    function loadUserHistory(email) {
        // Aquí llamaríamos a tu endpoint /list/{email}
        // Ejemplo con fetch:
        fetch(`/HistoryResource/list/${encodeURIComponent(email)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al cargar el historial');
                }
                return response.json();
            })
            .then(history => {
                displayHistory(history);
            })
            .catch(error => {
                console.error('Error al cargar historial:', error);
                document.getElementById('historyList').innerHTML = '<li>No se pudo cargar el historial</li>';
            });
    }

    function displayHistory(history) {
        const historyList = document.getElementById('historyList');

        if (!history || history.length === 0) {
            historyList.innerHTML = '<li>No hay historial reciente</li>';
            return;
        }

        let html = '';

        // Suponiendo que el historial contiene productos buscados
        history.forEach(item => {
            // Aquí deberías adaptar según la estructura real de tu History
            html += `
                <li class="history-item">
                    <strong>Producto ID:</strong> ${item.historyId.id_producto} |
                    <strong>Fecha:</strong> ${new Date().toLocaleDateString()}
                </li>
            `;
        });

        historyList.innerHTML = html;
    }