const API_URL = "http://localhost:8080/ListedProductResource/list/";

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
