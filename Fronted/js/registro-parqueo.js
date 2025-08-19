document.addEventListener("DOMContentLoaded", () => {
  // Inicializaciones al cargar la página
  cargarEspaciosDisponibles();
  cargarTarifasActivas();
  cargarPlacas();
  listarRegistrosActivos();
  cargarRegistrosSimples();

  // Evento submit formulario registro parqueo
  const formRegistro = document.getElementById("form-registro-parqueo");
  if (formRegistro) {
    formRegistro.addEventListener("submit", registrarParqueo);
  }

  // Evento cuando se selecciona una placa
  const inputPlaca = document.getElementById("placaEntrada");
  if (inputPlaca) {
    inputPlaca.addEventListener("change", buscarVehiculoPorPlaca);
  }
});


// 👉 Cargar espacios disponibles
async function cargarEspaciosDisponibles() {
  try {
    const response = await fetch("http://localhost:8081/api/espacios-parqueo/listar");
    if (!response.ok) throw new Error("Error al cargar espacios");
    const data = await response.json();

    const select = document.getElementById("espacioEntrada");
    if (!select) return;
    select.innerHTML = '<option value="">-- Selecciona un espacio --</option>';

    data.forEach(espacio => {
      const option = document.createElement("option");
      option.value = espacio.id;
      option.textContent = `${espacio.numero} - ${espacio.tipo}`;
      select.appendChild(option);
    });
  } catch (error) {
    console.error("❌ Error cargando espacios:", error);
  }
}


// 👉 Cargar tarifas activas
async function cargarTarifasActivas() {
  try {
    const response = await fetch("http://localhost:8081/api/tarifas/listar");
    if (!response.ok) throw new Error("Error al cargar tarifas");
    const data = await response.json();

    const select = document.getElementById("tarifaEntrada");
    if (!select) return;
    select.innerHTML = '<option value="">-- Selecciona una tarifa --</option>';

    data.forEach(tarifa => {
      const option = document.createElement("option");
      option.value = tarifa.id;
      option.textContent = `${tarifa.tipoVehiculo} - ${tarifa.tipoTiempo} ($${tarifa.valor})`;
      select.appendChild(option);
    });
  } catch (error) {
    console.error("❌ Error cargando tarifas:", error);
  }
}


// 👉 Buscar vehículo por placa
async function buscarVehiculoPorPlaca(e) {
  const placa = e.target.value.trim().toUpperCase();
  if (!placa) return;

  try {
    const response = await fetch(`http://localhost:8081/api/vehiculos/obtener/${placa}`);
    if (!response.ok) throw new Error("Vehículo no encontrado");
    const vehiculo = await response.json();
    console.log("🚗 Vehículo encontrado:", vehiculo);
    // Aquí puedes usar vehiculo.tipo si lo necesitas
  } catch (error) {
    alert(`Error al buscar placa ${placa}: ${error.message}`);
  }
}


// 👉 Cargar placas en datalist
async function cargarPlacas() {
  try {
    const response = await fetch("http://localhost:8081/api/vehiculos/listar");
    if (!response.ok) throw new Error("Error al cargar placas");
    const vehiculos = await response.json();

    const datalist = document.getElementById("listaPlacas");
    if (!datalist) return;
    datalist.innerHTML = "";

    vehiculos.forEach(v => {
      const option = document.createElement("option");
      option.value = v.placa;
      datalist.appendChild(option);
    });

    console.log("✅ Placas cargadas en autocompletado");
  } catch (error) {
    console.error("❌ Error al cargar placas:", error);
  }
}


// 👉 Registrar parqueo
async function registrarParqueo(event) {
  event.preventDefault();

  const mensaje = document.getElementById("mensajeEntrada");
  const placa = document.getElementById("placaEntrada").value.trim().toUpperCase();
  const espacioId = parseInt(document.getElementById("espacioEntrada").value);
  const tarifaId = parseInt(document.getElementById("tarifaEntrada").value);

  if (!placa || isNaN(espacioId) || isNaN(tarifaId)) {
    mensaje.textContent = "❌ Todos los campos son obligatorios y deben ser válidos.";
    mensaje.style.color = "red";
    return;
  }

  const data = {
    vehiculo: { placa },
    espacioParqueo: { id: espacioId },
    tarifa: { id: tarifaId }
  };

  try {
    const response = await fetch("http://localhost:8081/api/registros/crear", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      const text = await response.text();
      throw new Error(text || "Error desconocido del servidor");
    }

    await response.json();
    mensaje.textContent = "✅ Entrada registrada con éxito.";
    mensaje.style.color = "green";
    document.getElementById("form-registro-parqueo").reset();
    cargarEspaciosDisponibles();
    listarRegistrosActivos();
  } catch (error) {
    console.error("❌ Error al registrar entrada:", error);
    mensaje.textContent = "❌ No se pudo registrar la entrada.";
    mensaje.style.color = "red";
    alert("El vehículo ya se encuentra parqueado o el espacio seleccionado está ocupado.");
  }
}


// 👉 Listar registros activos
async function listarRegistrosActivos() {
  try {
    const response = await fetch("http://localhost:8081/api/registros/activos");
    if (!response.ok) throw new Error("Error al obtener registros activos");

    const registros = await response.json();
    const tabla = document.getElementById("tablaRegistrosActivos");
    if (!tabla) return;

    tabla.innerHTML = "";

    if (registros.length > 0) {
      const thead = document.createElement("thead");
      thead.innerHTML = `
        <tr>
          <th>Placa</th>
          <th>Espacio</th>
          <th>Tipo Vehículo</th>
          <th>Tarifa</th>
          <th>Usuario</th>
          <th>Hora Entrada</th>
        </tr>`;
      tabla.appendChild(thead);
    }

    const tbody = document.createElement("tbody");
    registros.forEach(r => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${r.vehiculo.placa}</td>
        <td>${r.espacioParqueo.numero} (${r.espacioParqueo.tipo})</td>
        <td>${r.vehiculo.tipo}</td>
        <td>${r.tarifa.tipoVehiculo} - ${r.tarifa.tipoTiempo} ($${r.tarifa.valor})</td>
        <td>${r.usuarioRegistro?.nombre || "N/A"} (${r.usuarioRegistro?.rol || "N/A"})</td>
        <td>${new Date(r.fechaEntrada).toLocaleString()}</td>`;
      tbody.appendChild(tr);
    });

    tabla.appendChild(tbody);
  } catch (error) {
    console.error("❌ Error al listar registros activos:", error);
  }
}


// 👉 Listar registros simples
async function cargarRegistrosSimples() {
  try {
    const res = await fetch("http://localhost:8081/api/registros/listar-simple");
    if (!res.ok) throw new Error("Error al obtener registros");
    const registros = await res.json();

    const tbody = document.querySelector("#tablaRegistrosSimple tbody");
    if (!tbody) return;
    tbody.innerHTML = "";

    registros.forEach(r => {
      const fila = document.createElement("tr");
      fila.innerHTML = `
        <td>${r.placa}</td>
        <td>${r.horaEntrada ? r.horaEntrada.replace("T", " ") : ""}</td>
        <td>${r.horaSalida ? r.horaSalida.replace("T", " ") : ""}</td>`;
      tbody.appendChild(fila);
    });
  } catch (err) {
    console.error("❌ Error cargando registros simples:", err);
  }
}




