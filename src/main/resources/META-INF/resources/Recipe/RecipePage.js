// --- Nuevo archivo: RecipePage.js ---

// Reutiliza la URL base de tu API

const API_URL = 'http://localhost:8080/recipe';

const token = localStorage.getItem("token");


// Elementos del DOM que vamos a actualizar

const recipeNameElement = document.querySelector('.recipeName');

const recipeImageElement = document.querySelector('.recipeImg');

const ratingElement = document.querySelector('.icons .item:nth-child(1) .text'); // Selecciona el texto de Rating

const kcalElement = document.querySelector('.icons .item:nth-child(2) .text');   // Selecciona el texto de Kcal

const timeElement = document.querySelector('.icons .item:nth-child(3) .text');    // Selecciona el texto de Mins

const ingredientsListElement = document.querySelector('.details .listado:nth-of-type(1)'); // Primera lista <ul>

const instructionsListElement = document.querySelector('.details .listado:nth-of-type(2)'); // Segunda lista <ul>

const mainElement = document.querySelector('.recipeMain'); // Para mostrar errores generales


async function loadRecipeDetails() {

    if (!token) {

        alert('No estás autenticado. Por favor, inicia sesión.');

        window.location.href = '../UserScreens/SignInPage.html'; // Ajusta la ruta si es necesario

        return;

    }


    // 1. Obtener el nombre de la receta de la URL

    const urlParams = new URLSearchParams(window.location.search);

    const recipeName = urlParams.get('name'); // Obtiene el valor decodificado


    if (!recipeName) {

        showError("No se especificó el nombre de la receta en la URL.");

        return;

    }


    // Muestra el nombre en el título mientras carga (opcional)

    if (recipeNameElement) {

        recipeNameElement.textContent = `Cargando "${recipeName}"...`;

    }

     // Limpia las listas anteriores

    if (ingredientsListElement) ingredientsListElement.innerHTML = '';

    if (instructionsListElement) instructionsListElement.innerHTML = '';


    try {

        // 2. Llamar a la API para obtener los detalles de ESTA receta

        // El nombre en el path debe ir codificado si contiene caracteres especiales

        const encodedNameForPath = encodeURIComponent(recipeName);

        const response = await fetch(`${API_URL}/${encodedNameForPath}`, {

            method: 'GET',

            headers: {

                'Authorization': `Bearer ${token}`,

                'Accept': 'application/json'

            }

        });


        if (response.status === 401 || response.status === 403) {

            alert('Tu sesión ha expirado o no tienes permisos. Por favor, inicia sesión de nuevo.');

            localStorage.removeItem("token");

            window.location.href = '../UserScreens/SignInPage.html';

            return;

        }


         if (response.status === 404) {

             showError(`Receta "${recipeName}" no encontrada.`);

             if (recipeNameElement) recipeNameElement.textContent = "Receta no encontrada";

             return;

         }


        if (!response.ok) {

            const errorText = await response.text();

            throw new Error(`Error al cargar la receta: ${response.status} ${response.statusText} - ${errorText}`);

        }


        const recipeData = await response.json();


        // 3. Rellenar la página con los datos

        if (recipeNameElement) {

            recipeNameElement.textContent = recipeData.recipeId?.nombre || "Nombre no disponible"; // Usa el nombre de recipeId

        }


        // --- Datos Reales del Backend ---

        if (ingredientsListElement) {

            // Asumiendo que 'ingredientes' es un string separado por comas
            const ingredients = recipeData.ingredientes ? recipeData.ingredientes.split(',') : ['No hay ingredientes listados'];

            ingredientsListElement.innerHTML = ingredients
                .map(item => capitalizeFirstLetter(item.trim())) // Capitaliza la primera letra
                .map(item => `<li>${escapeHtml(item)}</li>`) // Crea el <li> escapando HTML
                .join(''); // Une todo

        }


        if (instructionsListElement) {

            // Asumiendo que 'instrucciones' es un string separado por comas
            const instructions = recipeData.instrucciones ? recipeData.instrucciones.split(',') : ['No hay instrucciones listadas'];

            instructionsListElement.innerHTML = instructions
                .map(item => capitalizeFirstLetter(item.trim())) // Capitaliza la primera letra
                .map(item => `<li>${escapeHtml(item)}</li>`) // Crea el <li> escapando HTML
                .join(''); // Une todo

        }


        // --- Datos Mockeados ---

        if (recipeImageElement) {

             // Podrías tener una imagen por defecto o intentar cargar una basada en el nombre

            recipeImageElement.src = "../images/Gluten-free.png"; // Placeholder

            recipeImageElement.alt = `Imagen de ${recipeData.recipeId?.nombre || 'receta'}`;

        }

        if (ratingElement) {

            ratingElement.textContent = "4.5"; // Mock

             // Podrías mostrar las estrellas directamente: item.innerHTML = `<span class="emoji">⭐⭐⭐⭐✨</span> <span class="text">4.5</span>`

        }

        if (kcalElement) {

            kcalElement.textContent = "350 Kcal"; // Mock

        }

        if (timeElement) {

            timeElement.textContent = "30 Mins"; // Mock

        }


    } catch (error) {

        console.error('Error al cargar detalles de la receta:', error);

        showError(`No se pudieron cargar los detalles de la receta: ${error.message}`);

         if (recipeNameElement) recipeNameElement.textContent = "Error al cargar";

    }

}


// Función auxiliar para mostrar errores en la página

function showError(message) {

    if (mainElement) {

        const errorDiv = document.createElement('div');

        errorDiv.style.color = 'red';

        errorDiv.style.marginTop = '20px';

        errorDiv.style.padding = '10px';

        errorDiv.style.border = '1px solid red';

        errorDiv.textContent = message;

        // Inserta el error antes de la sección de información

        const infoSection = mainElement.querySelector('.info');

        if(infoSection) {

            mainElement.insertBefore(errorDiv, infoSection);

        } else {

             mainElement.appendChild(errorDiv);

        }

    } else {

        alert(message); // Fallback si no encuentra el main

    }

}


// Función simple para escapar HTML y prevenir XSS básico

function escapeHtml(unsafe) {

    if (!unsafe) return '';

    return unsafe

         .replace(/&/g, "&amp;")

         .replace(/</g, "&lt;")

         .replace(/>/g, "&gt;")

         .replace(/"/g, "&quot;")

         .replace(/'/g, "&#039;");

 }


// Función para capitalizar la primera letra de cada línea

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}


// Ejecutar la función cuando el DOM esté completamente cargado

window.addEventListener('DOMContentLoaded', loadRecipeDetails);
