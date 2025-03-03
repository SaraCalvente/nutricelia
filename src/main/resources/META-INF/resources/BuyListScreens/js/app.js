const API_URL = "http://localhost:8080/buyListResource/";

Vue.createApp({
    data() {
        return {
            buyLists: [],
            nombre: "",
            editingId: null,
            editedName: "",
        };
    },
    methods: {
        async fetchBuyLists() {
            try {
                let response = await axios.get(API_URL);
                this.buyLists = response.data;
            } catch (error) {
                console.error("Error obteniendo listas:", error);
            }
        },

        async createBuyList() {
            try {
                await axios.post(API_URL, { nombre: this.nombre });
                this.nombre = "";
                this.fetchBuyLists();
            } catch (error) {
                console.error("Error creando lista:", error);
            }
        },

        async deleteBuyList(id) {
            if (!confirm("¿Eliminar esta lista de compras?")) return;
            try {
                await axios.delete(API_URL + id);
                this.fetchBuyLists();
            } catch (error) {
                console.error("Error eliminando lista:", error);
            }
        },

        // Iniciar edición de una lista
        startEditing(list) {
            this.editingId = list.id;
            this.editedName = list.nombre;
        },

        // Guardar cambios en el nombre de la lista
        async saveEdit(id) {
            try {
                await axios.put(API_URL + id, { nombre: this.editedName });
                this.editingId = null;
                this.fetchBuyLists();
            } catch (error) {
                console.error("Error actualizando lista:", error);
            }
        }
    },
    mounted() {
        this.fetchBuyLists();
    }
}).mount("#app");