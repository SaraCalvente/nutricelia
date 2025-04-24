let productosComparar = [];

function guardarIdComparar(idProducto) {
    if (productosComparar.length === 1) {
        productosComparar.push(idProducto);
        window.location.href = `/product/${productosComparar[0]}/${productosComparar[1]}`;
    } else {
        productosComparar.push(idProducto);
        alert("Haz clic en otro producto para comparar");
    }
}