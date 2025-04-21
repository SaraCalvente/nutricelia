document.addEventListener('DOMContentLoaded', () => {
  const formularios = document.querySelectorAll('.formulario-anadir');

  formularios.forEach(form => {
    form.addEventListener('submit', async (event) => {
      event.preventDefault();

      const id_lista = form.querySelector('[name="id_lista"]').value;
      const id_producto = form.querySelector('[name="id_producto"]').value;

      try {
        const response = await fetch('/productsList', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            productsListId: {
              id_lista: parseInt(id_lista),
              id_producto: parseInt(id_producto)
            }
          })
        });

        if (response.ok) {
          console.log("Producto añadido. Redirigiendo...");
          window.location.href = `/ProductsList/ProductsList.html?idLista=${id_lista}`;
        } else {
          alert("Error al añadir producto.");
        }
      } catch (error) {
        console.error("Error de red:", error);
        alert("Error al comunicarse con el servidor.");
      }
    });
  });
});