const API_URL = 'http://localhost:8080/recipe';
const token = localStorage.getItem("token");

// Elementos del DOM
const recipeNameElement = document.querySelector('.recipeName');
const recipeImageElement = document.querySelector('.recipeImg');
const ratingElement = document.querySelector('.icons .item:nth-child(1) .text');
const kcalElement = document.querySelector('.icons .item:nth-child(2) .text');
const timeElement = document.querySelector('.icons .item:nth-child(3) .text');
const ingredientsListElement = document.querySelector('.details .listado:nth-of-type(1)');
const instructionsListElement = document.querySelector('.details .listado:nth-of-type(2)');
const mainElement = document.querySelector('.recipeMain');

async function loadRecipeDetails() {
    if (!token) {
        alert('No estás autenticado. Por favor, inicia sesión.');
        window.location.href = '../UserScreens/SignInPage.html';
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const recipeName = urlParams.get('name');

    if (!recipeName) {
        showError("No se especificó el nombre de la receta en la URL.");
        return;
    }

    if (recipeNameElement) {
        recipeNameElement.textContent = `Cargando "${recipeName}"...`;
    }

    if (ingredientsListElement) ingredientsListElement.innerHTML = '';
    if (instructionsListElement) instructionsListElement.innerHTML = '';

    try {
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

        // Actualizar la página con los datos
        if (recipeNameElement) {
            recipeNameElement.textContent = recipeData.recipeId?.nombre || "Nombre no disponible";
        }

        // Procesar ingredientes (convertir URLs en enlaces)
        if (ingredientsListElement) {
            const ingredients = recipeData.ingredientes ? recipeData.ingredientes.split('\n') : ['No hay ingredientes listados'];
            ingredientsListElement.innerHTML = ingredients
                .map(item => {
                    // Si el item es una URL, convertirlo en enlace
                    if (isUrl(item.trim())) {
                        return `<li><a href="${item.trim()}" target="_blank">${item.trim()}</a></li>`;
                    }
                    return `<li>${escapeHtml(capitalizeFirstLetter(item.trim()))}</li>`;
                })
                .join('');
        }

        // Procesar instrucciones (convertir URLs en enlaces)
        if (instructionsListElement) {
            const instructions = recipeData.instrucciones ? recipeData.instrucciones.split('\n') : ['No hay instrucciones listadas'];
            instructionsListElement.innerHTML = instructions
                .map(item => {
                    // Si el item es una URL, convertirlo en enlace
                    if (isUrl(item.trim())) {
                        return `<li><a href="${item.trim()}" target="_blank">${item.trim()}</a></li>`;
                    }
                    return `<li>${escapeHtml(capitalizeFirstLetter(item.trim()))}</li>`;
                })
                .join('');
        }

        // Datos mockeados (puedes reemplazar con datos reales si los tienes)
        if (recipeImageElement) {
            recipeImageElement.src = "../images/Gluten-free.png";
            recipeImageElement.alt = `Imagen de ${recipeData.recipeId?.nombre || 'receta'}`;
        }

        if (ratingElement) ratingElement.textContent = "4.5";
        if (kcalElement) kcalElement.textContent = "350 Kcal";
        if (timeElement) timeElement.textContent = "30 Mins";

    } catch (error) {
        console.error('Error al cargar detalles de la receta:', error);
        showError(`No se pudieron cargar los detalles de la receta: ${error.message}`);
        if (recipeNameElement) recipeNameElement.textContent = "Error al cargar";
    }
}

// Función para detectar si un texto es una URL
function isUrl(text) {
    try {
        new URL(text);
        return true;
    } catch (_) {
        return false;
    }
}

// Función auxiliar para mostrar errores
function showError(message) {
    if (mainElement) {
        const errorDiv = document.createElement('div');
        errorDiv.style.color = 'red';
        errorDiv.style.marginTop = '20px';
        errorDiv.style.padding = '10px';
        errorDiv.style.border = '1px solid red';
        errorDiv.textContent = message;

        const infoSection = mainElement.querySelector('.info');
        if(infoSection) {
            mainElement.insertBefore(errorDiv, infoSection);
        } else {
            mainElement.appendChild(errorDiv);
        }
    } else {
        alert(message);
    }
}

// Función para escapar HTML
function escapeHtml(unsafe) {
    if (!unsafe) return '';
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// Función para capitalizar la primera letra
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

// Ejecutar al cargar la página
window.addEventListener('DOMContentLoaded', loadRecipeDetails);