const API_URL = "http://localhost:8080/buyListResource";

Vue.createApp({
    data() {
        return {
            buyLists: [], // Lista de compras
            nombre: "" // Nombre de la nueva lista
        };
    },
    methods: {
        async fetchBuyLists() {
            try {
                let response = await axios.get(API_URL);
                this.buyLists = response.data;
            } catch (error) {
                console.error("Error obteniendo las listas:", error);
            }
        },

        async createBuyList() {
            if (!this.nombre.trim()) {
                alert("El nombre no puede estar vacío");
                return;
            }

            try {
                let nuevaLista = { nombre: this.nombre };
                await axios.post(API_URL, nuevaLista);
                this.nombre = ""; // Limpiar campo de texto
                this.fetchBuyLists(); // Recargar listas
            } catch (error) {
                console.error("Error creando la lista:", error);
            }
        },

        async deleteBuyList(id) {
            if (!confirm("¿Estás seguro de que quieres eliminar esta lista?")) return;

            try {
                await axios.delete(`${API_URL}/${id}`);
                this.fetchBuyLists(); // Recargar listas
            } catch (error) {
                console.error("Error eliminando la lista:", error);
            }
        }
    },
    mounted() {
        this.fetchBuyLists();
    }
}).mount("#app");