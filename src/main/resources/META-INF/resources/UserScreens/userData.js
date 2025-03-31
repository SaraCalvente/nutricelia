// Cargar datos del usuario desde el backend
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
                document.getElementById("user-name").textContent = user.nombre;
                document.getElementById("user-email").textContent = user.email;
            } else {
                const errorData = await response.json();
                alert("Error al cargar los datos del usuario: " + (errorData.message || "Error desconocido"));
            }
        } catch (error) {
            console.error("Error al obtener los datos:", error);
            alert("Hubo un error al intentar obtener los datos del usuario.");
        }
    }

    // Cerrar sesión y redirigir a SignOutPage.html
    function logout() {
        localStorage.removeItem("token"); // Eliminar token JWT
        window.location.href = "SignOutPage.html"; // Redirigir a página de cierre de sesión
    }

    // Confirmar y eliminar cuenta
    async function deleteAccount() {
    const email = document.getElementById("user-email").textContent.trim();  // Obtener el email correctamente
    console.log("Email a enviar:", email);  // Verifica el valor del email en la consola

    if (email === "") {
        alert("No se encontró el email.");
        return;
    }

    const confirmDelete = confirm("¿Estás seguro de que quieres eliminar tu cuenta? \nEsta acción no se puede deshacer.");

    if (confirmDelete) {
        try {
            // Asegúrate de que la URL esté correctamente formada
            const response = await fetch(`http://localhost:8080/user/delete/${email}`, {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("token"),
                    "Content-Type": "application/json"
                }
            });

            if (response.ok) {
                alert("Cuenta eliminada exitosamente.");
                localStorage.removeItem("token"); // Limpiar datos del usuario
                window.location.href = "DeletedPage.html"; // Redirigir tras eliminar cuenta
            } else {
                alert("Error al eliminar la cuenta.");
            }
        } catch (error) {
            console.error("Error al eliminar la cuenta:", error);
        }
    }
}

function modifyAccount() {
    document.getElementById("editPopup").style.display = "block";
    document.getElementById("popupOverlay").style.display = "block"; // Muestra el fondo oscuro
    document.body.classList.add("no-scroll"); // Bloquea el scroll del fondo
}

function closePopup() {
    document.getElementById("editPopup").style.display = "none";
    document.getElementById("popupOverlay").style.display = "none"; // Oculta el fondo oscuro
    document.body.classList.remove("no-scroll"); // Permite nuevamente el scroll
}

async function saveChanges() {
    const newName = document.getElementById("new-name").value.trim();
    const email = document.getElementById("user-email").textContent.trim();

    if (!newName) {
        alert("El nombre no puede estar vacío.");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/user/modify/${email}`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token"),
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nombre: newName })
        });

        if (response.ok) {
            alert("Nombre actualizado con éxito.");
            document.getElementById("user-name").textContent = newName;
            closePopup();
        } else {
            alert("Error al actualizar el nombre.");
        }
    } catch (error) {
        console.error("Error al actualizar el nombre:", error);
    }
}
    // Cargar datos del usuario al abrir la página
    loadUserData();