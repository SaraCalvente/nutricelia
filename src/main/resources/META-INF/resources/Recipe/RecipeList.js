const API_URL = 'http://localhost:8080/recipe';

// Obtener el token del localStorage
const token = localStorage.getItem("token");

if (!token) {
    alert('No estás autenticado. Por favor, inicia sesión.');
    // Asegúrate que la ruta al login sea correcta desde la ubicación de RecipeListPage.html
    window.location.href = '../UserScreens/SignInPage.html';
}

async function loadRecipes() {
    const tableBody = document.querySelector("table tbody"); // Selecciona el cuerpo de la tabla
    if (!tableBody) {
      console.error("Error: No se encontró el elemento <tbody> en la tabla.");
      return;
    }
    // Limpia la tabla antes de cargar nuevos datos (opcional, pero bueno si se recarga)
    tableBody.innerHTML = '';

    try {
        const response = await fetch(API_URL, {
            method: 'GET', // Aunque GET es el default, es bueno ser explícito
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json' // Buena práctica indicar qué esperas recibir
            }
        });

        if (response.status === 401 || response.status === 403) {
             alert('Tu sesión ha expirado o no tienes permisos. Por favor, inicia sesión de nuevo.');
             localStorage.removeItem("token"); // Limpia el token inválido
             window.location.href = '../UserScreens/SignInPage.html';
             return; // Detiene la ejecución
        }

        if (!response.ok) {
            // Intenta leer el mensaje de error si el backend lo envía como texto
            const errorText = await response.text();
            throw new Error(`Error al cargar recetas: ${response.status} ${response.statusText} - ${errorText}`);
        }

        const recipes = await response.json();

        if (recipes.length === 0) {
            // Muestra un mensaje si no hay recetas
            const row = tableBody.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 3; // Ocupa todas las columnas
            cell.textContent = 'No tienes recetas guardadas todavía.';
            cell.style.textAlign = 'center';
        } else {
            recipes.forEach(recipe => {
                // Asegúrate de que recipe.recipeId existe antes de intentar acceder a sus propiedades
                if (!recipe.recipeId) {
                    console.error("Receta recibida sin recipeId:", recipe);
                    return; // Salta esta receta si no tiene ID
                }

                const row = tableBody.insertRow(); // Inserta fila en el tbody

                const nameCell = row.insertCell();
                    const recipeLink = document.createElement('a');
                    const encodedName = encodeURIComponent(recipe.recipeId.nombre); // Codifica el nombre para la URL
                    recipeLink.href = `RecipePage.html?name=${encodedName}`; // Enlace a la página de detalle con el nombre como parámetro
                    recipeLink.textContent = recipe.recipeId.nombre; // Texto visible del enlace
                    nameCell.appendChild(recipeLink); // Añade el enlace a la celda
                    // --- FIN DE LA MODIFICACIÓN ---

                    const ingredientsCell = row.insertCell();
                    // Podrías acortar los ingredientes si son muy largos para la lista
                    const MAX_INGREDIENTS_LENGTH = 50; // Ejemplo: mostrar solo 50 caracteres
                    ingredientsCell.textContent = recipe.ingredientes.length > MAX_INGREDIENTS_LENGTH
                        ? recipe.ingredientes.substring(0, MAX_INGREDIENTS_LENGTH) + '...'
                        : recipe.ingredientes;
                    ingredientsCell.title = recipe.ingredientes; // Muestra completo en hover

                    const optionsCell = row.insertCell();
                    optionsCell.innerHTML = `
                        <button class="delete-btn" data-name="${recipe.recipeId.nombre}"></button>
                    `;
            });
        }

         // Añadir listeners a los botones DESPUÉS de crear todas las filas
         document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', async (e) => {
                const name = e.target.getAttribute('data-name');
                 if (confirm(`¿Estás seguro de que quieres eliminar la receta "${name}"?`)) {
                    await deleteRecipe(name);
                    // Podrías recargar la lista o quitar solo la fila
                    // e.target.closest('tr').remove(); // Quita fila de la tabla
                    loadRecipes(); // Recarga la lista completa para asegurar consistencia
                 }
            });
        });


    } catch (error) {
        console.error('Error en loadRecipes:', error);
        alert(`Hubo un problema al cargar tus recetas: ${error.message}`);
        // Podrías mostrar el error en la página en lugar de un alert
        const tableBody = document.querySelector("table tbody");
        if (tableBody) {
             tableBody.innerHTML = `<tr><td colspan="3" style="color: red; text-align: center;">Error al cargar recetas: ${error.message}</td></tr>`;
        }
    }
}

async function deleteRecipe(recipeName) {
    try {
        const encodedName = encodeURIComponent(recipeName);
        const response = await fetch(`${API_URL}/delete/${encodedName}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

         if (response.status === 401 || response.status === 403) {
             alert('Tu sesión ha expirado o no tienes permisos. Por favor, inicia sesión de nuevo.');
             localStorage.removeItem("token");
             window.location.href = '../UserScreens/SignInPage.html';
             return;
        }

        if (!response.ok) {
             const errorText = await response.text();
             alert(`Error al eliminar la receta: ${response.status} ${response.statusText} - ${errorText}`);
             throw new Error(`Error al eliminar: ${response.status}`); // Lanza error para detener
        } else {
            console.log(`Receta "${recipeName}" eliminada`);
            // No es necesario quitar la fila aquí si vas a recargar la lista completa
        }
    } catch (error) {
        console.error('Error en deleteRecipe:', error);
        // El alert ya se mostró si falló el fetch
    }
}

window.addEventListener('DOMContentLoaded', loadRecipes);