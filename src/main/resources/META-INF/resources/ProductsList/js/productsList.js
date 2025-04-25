// Obtener parámetros de la URL
function obtenerParametroURL(nombre) {
    const params = new URLSearchParams(window.location.search);
    return params.get(nombre);
}

// Obtener el idLista de la URL
const idLista = obtenerParametroURL('idLista') || 1;

// Id de la lista que a cargar
const apiBaseUrl = 'http://localhost:8080';

var userEmail = null;

document.addEventListener("DOMContentLoaded", loadUserData);

// Cargar productos con el idLista correcto
document.addEventListener('DOMContentLoaded', () => {
    cargarProductos(idLista);
});


// Función para obtener y renderizar la lista de productos
async function cargarProductos(idLista) {
    try {
        const responseProductos = await fetch(`${apiBaseUrl}/productsList/list/${idLista}`);
        const productos = await responseProductos.json();

        const responseLista = await fetch(`${apiBaseUrl}/buyListResource/${idLista}`);
        const listaNombre = await responseLista.json(); //Para acceder al nombres es con .nombre
        await renderProductos(productos);
        await renderNombreLista(listaNombre);
        //falta pillar los datos del supermercado (el nombre)
    } catch (error) {
        console.error('Error al cargar datos:', error);
    }
}

// Función para cargar los datos del usuario
async function loadUserData() {
    try {

        const token = localStorage.getItem("token");
        if (!token) {
            alert("No se encontró el token de autenticación.");
            return;
        }

        const response = await fetch("http://localhost:8080/user/me", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const user = await response.json();

            userEmail = user.email;
        } else {
            const errorData = await response.json();
            alert("Error al cargar los datos del usuario: " + (errorData.message || "Error desconocido"));
        }
    } catch (error) {
        console.error("Error al obtener los datos:", error);
        alert("Hubo un error al intentar obtener los datos del usuario.");
    }
}

// Renderizar el nombre de la lista
async function renderNombreLista(listaNombre) {
    const h2 = document.getElementById('nombre-lista');
    h2.innerHTML = '';
    h2.textContent = 'Lista '+listaNombre.nombre;
}

// Renderizar los productos en la tabla
async function renderProductos(productos) {
    const tbody = document.getElementById('productos-body');
    tbody.innerHTML = ''; // Limpiar antes de renderizar

    if (productos.length !== 0){
        for (let item of productos) {
          const tr = document.createElement('tr');

          // Columna Producto
          const tdProducto = document.createElement('td');
          tdProducto.textContent = `${await obtainProductName(item.productsListId.id_producto)}`;

          // Columna Supermercado
          const tdSupermercado = document.createElement('td');
          tdSupermercado.textContent = 'Mercadona'; // Aquí debería ir el supermercado del producto pero ya se hará

          // Columna Opciones
          const tdOpciones = document.createElement('td');

          // Botón Eliminar
          const btnEliminar = document.createElement('button');
          btnEliminar.textContent = '❌';
          btnEliminar.onclick = () => eliminarProducto(item.productsListId.id_lista, item.productsListId.id_producto);
          btnEliminar.classList.add('btn-opciones-prod');

          // Botón Marcar como Comprado (puedes definir su lógica)
          const btnMarcar = document.createElement('button');
          btnMarcar.textContent = '🛒';
          btnMarcar.onclick = () => marcarComoComprado(item.productsListId.id_lista, item.productsListId.id_producto);
          btnMarcar.classList.add('btn-opciones-prod');

          tdOpciones.appendChild(btnEliminar);
          tdOpciones.appendChild(btnMarcar);

          // Agregar columnas a la fila
          tr.appendChild(tdProducto);
          tr.appendChild(tdSupermercado);
          tr.appendChild(tdOpciones);

          tbody.appendChild(tr);
        };
    } else {
        const tr = document.createElement('tr');

        const tdProducto = document.createElement('td');

        const tdSupermercado = document.createElement('td');
        tdSupermercado.textContent = 'No hay productos'

        const tdOpciones = document.createElement('td');

        tr.appendChild(tdProducto);
        tr.appendChild(tdSupermercado);
        tr.appendChild(tdOpciones);

        tbody.appendChild(tr);
    }
}

// Función para eliminar un producto
async function eliminarProducto(id_lista, id_producto) {
if (!confirm('¿Estás seguro de eliminar este producto?')) return;

try {
  const response = await fetch(`${apiBaseUrl}/productsList/${id_lista}/${id_producto}`, {
    method: 'DELETE'
  });

  if (response.ok) {
    alert('Producto eliminado');
    await cargarProductos(idLista); // Recargar productos después de eliminar
  } else {
    alert('Error al eliminar producto');
  }
} catch (error) {
  console.error('Error al eliminar producto:', error);
}
}

// Función para marcar como comprado
async function marcarComoComprado(id_lista, id_producto) {

    try {
        // Paso 1: Añadir al historial
        const historyEntry = {
            historyId: {
                email: userEmail, // Asegúrate que `userEmail` esté definido globalmente
                id_producto: id_producto
            }
        };

        const historyResponse = await fetch("http://localhost:8080/HistoryResource", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(historyEntry)
        });

        if (!historyResponse.ok) {
            throw new Error("No se pudo añadir el producto al historial.");
        }

        // Paso 2: Eliminar de la lista
        const response = await fetch(`${apiBaseUrl}/productsList/${id_lista}/${id_producto}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Producto eliminado');
            await cargarProductos(idLista); // Recargar productos después de eliminar
        } else {
            alert('Error al eliminar producto');
        }
    } catch (error) {
        console.error("Error al marcar como comprado:", error);
        alert("Error al procesar el producto.", error);
    }

//Se puede medio copiar el método de arriba de eliminar producto, solo hay que cambiar la función a la que se llama y el texto de las alertas y confirms

// Aquí irá la opción de añadir confirmar que se ha comprado y se guardará en el historial
//Cambiar el alert anterior para que se haga después de guardarlo en el historial

}

async function obtainProductName(id_producto){
    const responseNombreProductos = await fetch(`${apiBaseUrl}/product/name/${id_producto}`);
    const producto = await responseNombreProductos.json();
    return producto.nombre;
}

// Función de busqueda
document.getElementById('busqueda_productos').addEventListener('submit', function(event) {
  event.preventDefault(); // Evitar que se recargue la página

  const input = this.querySelector('input[name="producto"]');
  const cadena = encodeURIComponent(input.value); // Por si contiene espacios o caracteres raros

  if (!cadena) {
    alert("Escribe algo para buscar.");
    return;
  }

  // Redirigir a la vista
  window.location.href = `/product/search/${cadena}?idLista=${idLista}`;

});