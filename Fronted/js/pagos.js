document.addEventListener("DOMContentLoaded", () => {
    const formPago = document.getElementById("formPago");
    const tablaPagosBody = document.getElementById("tablaPagosBody");

    // Renderizar pagos en la tabla
    function renderizarPagos(pagos) {
        tablaPagosBody.innerHTML = "";
        pagos.forEach(pago => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${pago.registroParqueo.vehiculo.placa}</td>
                <td>${pago.monto}</td>
                <td>${pago.metodoPago}</td>
                <td>${new Date(pago.fechaPago).toLocaleString()}</td>
            `;
            tablaPagosBody.appendChild(fila);
        });
    }

    // Obtener pagos del backend
    async function obtenerPagos() {
        try {
            const res = await fetch("http://localhost:8081/api/pagos/listar");
            if (!res.ok) throw new Error("No se pudo obtener los pagos");
            const pagos = await res.json();
            renderizarPagos(pagos);
        } catch (err) {
            console.error("Error al cargar pagos:", err.message);
        }
    }

    // Registrar un pago
    formPago.addEventListener("submit", async (e) => {
        e.preventDefault();

        const placa = document.getElementById("placaPago").value.trim();
        const metodoPago = document.getElementById("metodoPago").value;

        // Validación simple
        if (!placa || !metodoPago) {
            alert("Debe ingresar la placa y seleccionar un método de pago");
            return;
        }

        try {
            const res = await fetch(`http://localhost:8081/api/pagos/pagar/${placa}?metodoPago=${metodoPago}`, {
                method: "POST"
            });

            if (!res.ok) {
                const error = await res.json();
                throw new Error(error.message || "Error al procesar el pago");
            }

            const pago = await res.json();
            alert(`✅ Pago realizado correctamente. Monto: ${pago.monto}`);

            obtenerPagos();
            formPago.reset();
        } catch (err) {
            console.error("Error al procesar pago:", err.message);
            alert(`❌ Error: ${err.message}`);
        }
    });

    // Cargar historial al inicio
    obtenerPagos();
});
