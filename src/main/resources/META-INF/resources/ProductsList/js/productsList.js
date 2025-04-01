// Obtener parámetros de la URL
function obtenerParametroURL(nombre) {
    const params = new URLSearchParams(window.location.search);
    return params.get(nombre);
}

// Obtener el idLista de la URL
const idLista = obtenerParametroURL('idLista') || 1;

// Id de la lista que a cargar
const apiBaseUrl = 'http://localhost:8080';



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
        const listaNombre = await responseLista.json().nombre
        renderProductos(productos);
        //falta hacer el render del nombre de la lista
        //falta pillar los datos del supermercado (el nombre)
    } catch (error) {
        console.error('Error al cargar datos:', error);
    }
}

// Renderizar los productos en la tabla
function renderProductos(productos) {
const tbody = document.getElementById('productos-body');
tbody.innerHTML = ''; // Limpiar antes de renderizar

productos.forEach(item => {
  const tr = document.createElement('tr');

  // Columna Producto
  const tdProducto = document.createElement('td');
  tdProducto.textContent = `Producto ${item.productsListId.nombre}`;

  // Columna Supermercado
  const tdSupermercado = document.createElement('td');
  tdSupermercado.textContent = 'Mercadona'; // Aquí debería ir el supermercado del producto

  // Columna Opciones
  const tdOpciones = document.createElement('td');

  // Botón Eliminar
  const btnEliminar = document.createElement('button');
  btnEliminar.textContent = 'Eliminar';
  btnEliminar.onclick = () => eliminarProducto(item.productsListId.id_lista, item.productsListId.id_producto);

  // Botón Marcar como Comprado (puedes definir su lógica)
  const btnMarcar = document.createElement('button');
  btnMarcar.textContent = 'Marcar como Comprado';
  btnMarcar.onclick = () => marcarComoComprado(item.productsListId.id_lista, item.productsListId.id_producto);

  tdOpciones.appendChild(btnEliminar);
  tdOpciones.appendChild(btnMarcar);

  // Agregar columnas a la fila
  tr.appendChild(tdProducto);
  tr.appendChild(tdCantidad);
  tr.appendChild(tdOpciones);

  tbody.appendChild(tr);
});
}

// Función para eliminar un producto
async function eliminarProducto(id_lista, id_producto) {
if (!confirm('¿Estás seguro de eliminar este producto?')) return;

try {
  const response = await fetch(`${apiBaseUrl}/${id_lista}/${id_producto}`, {
    method: 'DELETE'
  });

  if (response.ok) {
    alert('Producto eliminado');
    cargarProductos(idLista); // Recargar productos después de eliminar
  } else {
    alert('Error al eliminar producto');
  }
} catch (error) {
  console.error('Error al eliminar producto:', error);
}
}

// Función para marcar como comprado
function marcarComoComprado(id_lista, id_producto) {
alert(`Producto ${id_producto} de la lista ${id_lista} marcado como comprado (esto es solo un placeholder)`);

// Aquí irá la opción de añadir confirmar que se ha comprado y se guardará en el historial

}

// Función de busqueda -> cambiarlo para que busque productos con menú desplegable
/*
const formBusqueda = document.getElementById('busqueda_productos');
formBusqueda.addEventListener('submit', (event) => {
    event.preventDefault();
    const query = formBusqueda.producto.value.toLowerCase();

    const filas = document.querySelectorAll('#productos-body tr');
    filas.forEach(fila => {
        const productoTexto = fila.querySelector('td').textContent.toLowerCase();
        fila.style.display = productoTexto.includes(query) ? '' : 'none';
    });
});
*/