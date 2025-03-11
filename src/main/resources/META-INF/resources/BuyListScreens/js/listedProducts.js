const API_URL = "http://localhost:8080/ListedProductResource/list/";
const HISTORY_API_URL = "http://localhost:8080/HistoryResource/";

Vue.createApp({
    data() {
        return {
            id_lista: null,
            listaNombre: "Cargando...",
            products: []
        };
    },
    methods: {
        async fetchProducts() {
            try {
                let response = await axios.get(API_URL + this.id_lista);
                this.products = response.data;
            } catch (error) {
                console.error("Error obteniendo los productos:", error);
            }
        },

        async deleteProduct(id) {
            if (!confirm("¿Estás seguro de eliminar este producto?")) return;

            try {
                await axios.delete(`http://localhost:8080/ListedProductResource/${id}`);
                this.fetchProducts();
            } catch (error) {
                console.error("Error eliminando producto:", error);
            }
        },

        async markAsPurchased(product) {
            if (!confirm(`¿Marcar "${product.nombre}" como comprado?`)) return;

            try {
                // Obtener el email del usuario (simulado)
                const email = localStorage.getItem("userEmail") || "usuario@example.com";

                // 1. Añadir al historial de compras
                await axios.post(HISTORY_API_URL, {
                    userMail: email,
                    productId: product.id,
                    nombre: product.nombre,
                    cantidad: product.cantidad
                });

                // 2. Eliminar de la lista de compras
                await axios.delete(`http://localhost:8080/ListedProductResource/${product.id}`);

                this.fetchProducts();
            } catch (error) {
                console.error("Error marcando producto como comprado:", error);
            }
        }
    },
    mounted() {
        const params = new URLSearchParams(window.location.search);
        this.id_lista = params.get("id_lista");

        if (!this.id_lista) {
            alert("Error: No se ha seleccionado una lista válida.");
            window.location.href = "ShoppingListScreen.html";
        } else {
            this.fetchProducts();
        }
    }
}).mount("#app");
