const API_URL = "http://localhost:8080/HistoryResource/list/";

Vue.createApp({
    data() {
        return {
            email: "",  // Se debe obtener de la sesión del usuario
            products: []
        };
    },
    methods: {
        async fetchHistory() {
            if (!this.email) {
                console.error("No se encontró un usuario logueado.");
                return;
            }

            try {
                let response = await axios.get(API_URL + this.email);
                this.products = response.data;
            } catch (error) {
                console.error("Error obteniendo historial:", error);
            }
        }
    },
    mounted() {
        // Simulación: En un caso real, obtendrás el email de la sesión del usuario
        this.email = localStorage.getItem("userEmail") || "usuario@example.com";
        this.fetchHistory();
    }
}).mount("#app");
