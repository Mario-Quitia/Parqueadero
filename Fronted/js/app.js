document.addEventListener("DOMContentLoaded", () => {
  // --- Referencias DOM generales ---
  const links = document.querySelectorAll(".sidebar a");
  const sections = document.querySelectorAll(".section-container");

  // --- VEHÍCULOS ---
  const vehiculoForm = document.getElementById("vehiculo-form");
  const tablaVehiculosBody = document.querySelector("#tabla-vehiculos tbody");
  const totalVehiculos = document.getElementById("total-vehiculos");

  // Modal editar vehículo
  const modalVehiculo = document.getElementById("modal-editar");
  const formEditarVehiculo = document.getElementById("form-editar-vehiculo");
  const btnCerrarModalVehiculo = document.getElementById("cerrar-modal");

  let editandoVehiculo = false;
  let placaOriginal = null;

  // --- TARIFAS ---
  const tarifaForm = document.getElementById("tarifa-form");
  const tablaTarifasBody = document.querySelector("#tabla-tarifas tbody");
  const totalTarifas = document.getElementById("total-tarifas");

  // Modal editar tarifa
  const modalTarifa = document.getElementById("modal-editar-tarifa");
  const formEditarTarifa = document.getElementById("form-editar-tarifa");
  const btnCerrarModalTarifa = document.getElementById("cerrar-modal-editar");

  let editandoTarifa = false;
  let idTarifaOriginal = null;

///Espacios

  const tablaEspaciosBody = document.querySelector("#tabla-espacios tbody");
  const gridEspacios = document.getElementById("grid-espacios");
  const btnEliminarSeleccionados = document.getElementById("btn-eliminar-seleccionados");
  const contadorSeleccionados = document.getElementById("contador-seleccionados");
    let espaciosSeleccionados = new Set();




  // --- Funciones generales ---

  // Navegación entre secciones
  links.forEach(link => {
    link.addEventListener("click", e => {
      e.preventDefault();
      const sectionId = link.getAttribute("data-section");

      links.forEach(l => l.classList.remove("active"));
      link.classList.add("active");

      sections.forEach(section => {
        section.classList.toggle("active", section.id === sectionId);
      });
    });
  });

  // --- MODAL VEHÍCULO ---
  function mostrarModalVehiculo() {
    modalVehiculo.classList.remove("hidden");
  }
  function ocultarModalVehiculo() {
    modalVehiculo.classList.add("hidden");
  }
  btnCerrarModalVehiculo.addEventListener("click", ocultarModalVehiculo);

  // --- MODAL TARIFA ---
  function mostrarModalTarifa() {
    modalTarifa.classList.remove("hidden");
  }
  function ocultarModalTarifa() {
    modalTarifa.classList.add("hidden");
  }
  btnCerrarModalTarifa.addEventListener("click", ocultarModalTarifa);

  // --- VEHÍCULOS ---

  // Cargar listado vehículos
  async function fetchVehiculos() {
    try {
      const res = await fetch("http://localhost:8081/api/vehiculos/listar");
      if (!res.ok) throw new Error("No se pudo obtener la lista de vehículos");
      const vehiculos = await res.json();

      tablaVehiculosBody.innerHTML = "";
      vehiculos.forEach(v => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
          <td>${v.placa}</td>
          <td>${v.tipo}</td>
          <td>${v.color}</td>
          <td>${v.marca}</td>
          <td>${v.modelo}</td>
          <td>
            <button class="editar-vehiculo-btn" data-placa="${v.placa}">✏️</button>
            <button class="borrar-vehiculo-btn" data-placa="${v.placa}">🗑️</button>
          </td>
        `;
        tablaVehiculosBody.appendChild(fila);
      });

      totalVehiculos.textContent = vehiculos.length;

      // Agregar eventos botones
      document.querySelectorAll(".editar-vehiculo-btn").forEach(btn => {
        btn.addEventListener("click", () => cargarVehiculo(btn.dataset.placa));
      });

      document.querySelectorAll(".borrar-vehiculo-btn").forEach(btn => {
        btn.addEventListener("click", () => eliminarVehiculo(btn.dataset.placa));
      });
    } catch (err) {
      alert("Error al cargar vehículos: " + err.message);
    }
  }

  // Cargar vehículo para editar (modal)
  async function cargarVehiculo(placa) {
    try {
      const res = await fetch(`http://localhost:8081/api/vehiculos/obtener/${placa}`);
      if (!res.ok) throw new Error("Vehículo no encontrado");
      const v = await res.json();

      document.getElementById("editar-placa").value = v.placa;
      document.getElementById("editar-tipo").value = v.tipo;
      document.getElementById("editar-color").value = v.color;
      document.getElementById("editar-marca").value = v.marca;
      document.getElementById("editar-modelo").value = v.modelo;

      placaOriginal = v.placa;
      editandoVehiculo = true;

      mostrarModalVehiculo();

      // Cambiar a sección vehículos si quieres:
      document.querySelector('[data-section="listado"]').click();
    } catch (err) {
      alert("Error al cargar vehículo: " + err.message);
    }
  }

  // Guardar vehículo (crear o editar)
  vehiculoForm.addEventListener("submit", async e => {
    e.preventDefault();

    const vehiculo = {
      placa: document.getElementById("placa").value,
      tipo: document.getElementById("tipo").value,
      color: document.getElementById("color").value,
      marca: document.getElementById("marca").value,
      modelo: document.getElementById("modelo").value,
    };

    try {
      let res;
      if (editandoVehiculo) {
        res = await fetch(`http://localhost:8081/api/vehiculos/editar/${placaOriginal}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(vehiculo)
        });
      } else {
        res = await fetch("http://localhost:8081/api/vehiculos/crear", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(vehiculo)
        });
      }

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText);
      }

      alert("Vehículo guardado correctamente");
      vehiculoForm.reset();
      editandoVehiculo = false;
      placaOriginal = null;
      await fetchVehiculos();

    } catch (err) {
      alert("Error al guardar vehículo: " + err.message);
    }
  });

  // Guardar edición vehículo desde modal
  formEditarVehiculo.addEventListener("submit", async e => {
    e.preventDefault();

    const vehiculoEditado = {
      tipo: document.getElementById("editar-tipo").value,
      color: document.getElementById("editar-color").value,
      marca: document.getElementById("editar-marca").value,
      modelo: document.getElementById("editar-modelo").value,
    };

    const placa = document.getElementById("editar-placa").value;

    try {
      const res = await fetch(`http://localhost:8081/api/vehiculos/editar/${placa}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(vehiculoEditado)
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText);
      }

      alert("Vehículo actualizado correctamente");
      ocultarModalVehiculo();
      await fetchVehiculos();

    } catch (err) {
      alert("Error al actualizar vehículo: " + err.message);
    }
  });

  // Eliminar vehículo
  async function eliminarVehiculo(placa) {
    if (!confirm("¿Seguro que deseas eliminar este vehículo?")) return;

    try {
      const res = await fetch(`http://localhost:8081/api/vehiculos/eliminar/${placa}`, {
        method: "DELETE"
      });

      if (!res.ok && res.status !== 204) throw new Error("No se pudo eliminar el vehículo");

      alert("Vehículo eliminado");
      await fetchVehiculos();
    } catch (err) {
      alert("Error al eliminar vehículo: " + err.message);
    }
  }

  // --- TARIFAS ---

  // Listar tarifas
  async function fetchTarifas() {
    try {
      const res = await fetch("http://localhost:8081/api/tarifas/listar");
      if (!res.ok) throw new Error("Error al obtener tarifas");
      const tarifas = await res.json();

      tablaTarifasBody.innerHTML = "";
      tarifas.forEach(t => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
          <td>${t.tipoVehiculo}</td>
          <td>${t.tipoTiempo}</td>
          <td>${t.valorHora}</td>
          <td>${t.valor}</td>
          <td>${t.valorFraccion ?? "-"}</td>
          <td>${t.activo ? "Sí" : "No"}</td>
          <td>
            <button class="editar-tarifa-btn" data-id="${t.id}">✏️</button>
            <button class="borrar-tarifa-btn" data-id="${t.id}">🗑️</button>
          </td>
        `;
        tablaTarifasBody.appendChild(fila);
      });

      totalTarifas.textContent = tarifas.length;

      document.querySelectorAll(".editar-tarifa-btn").forEach(btn => {
        btn.addEventListener("click", () => cargarTarifa(btn.dataset.id));
      });

      document.querySelectorAll(".borrar-tarifa-btn").forEach(btn => {
        btn.addEventListener("click", () => eliminarTarifa(btn.dataset.id));
      });

    } catch (err) {
      alert("Error al cargar tarifas: " + err.message);
    }
  }

  // Cargar tarifa para editar (modal)
  async function cargarTarifa(id) {
    try {
      const res = await fetch(`http://localhost:8081/api/tarifas/ver/${id}`);
      if (!res.ok) throw new Error("Tarifa no encontrada");
      const t = await res.json();

      document.getElementById("editar-tipoVehiculo").value = t.tipoVehiculo;
      document.getElementById("editar-tipoTiempo").value = t.tipoTiempo;
      document.getElementById("editar-valorHora").value = t.valorHora;
      document.getElementById("editar-valor").value = t.valor;
      document.getElementById("editar-valorFraccion").value = t.valorFraccion ?? "";
      document.getElementById("editar-activo").checked = t.activo;

      idTarifaOriginal = t.id;
      editandoTarifa = true;

      mostrarModalTarifa();

      // Cambiar a sección tarifas si usas navegación
      document.querySelector('[data-section="tarifas"]').click();

    } catch (err) {
      alert("Error al cargar tarifa: " + err.message);
    }
  }

  // Crear nueva tarifa
  tarifaForm.addEventListener("submit", async e => {
    e.preventDefault();

    const tarifa = {
      tipoVehiculo: document.getElementById("tipoVehiculo").value,
      tipoTiempo: document.getElementById("tipoTiempo").value,
      valorHora: parseFloat(document.getElementById("valorHora").value),
      valor: parseFloat(document.getElementById("valor").value),
      valorFraccion: parseFloat(document.getElementById("valorFraccion").value) || 0,
      activo: document.getElementById("activo").checked
    };

    try {
      const res = await fetch("http://localhost:8081/api/tarifas/crear", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(tarifa)
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText);
      }

      alert("Tarifa creada correctamente");
      tarifaForm.reset();
      await fetchTarifas();

    } catch (err) {
      alert("Error al crear tarifa: " + err.message);
    }
  });

  // Actualizar tarifa desde modal
  formEditarTarifa.addEventListener("submit", async e => {
    e.preventDefault();

    const tarifaActualizada = {
      tipoVehiculo: document.getElementById("editar-tipoVehiculo").value,
      tipoTiempo: document.getElementById("editar-tipoTiempo").value,
      valorHora: parseFloat(document.getElementById("editar-valorHora").value),
      valor: parseFloat(document.getElementById("editar-valor").value),
      valorFraccion: parseFloat(document.getElementById("editar-valorFraccion").value) || 0,
      activo: document.getElementById("editar-activo").checked
    };

    try {
      const res = await fetch(`http://localhost:8081/api/tarifas/editar/${idTarifaOriginal}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(tarifaActualizada)
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText);
      }

      alert("Tarifa actualizada correctamente");
      ocultarModalTarifa();
      editandoTarifa = false;
      idTarifaOriginal = null;
      await fetchTarifas();

    } catch (err) {
      alert("Error al actualizar tarifa: " + err.message);
    }
  });

  // Eliminar tarifa
  async function eliminarTarifa(id) {
    if (!confirm("¿Estás seguro de eliminar esta tarifa?")) return;

    try {
      const res = await fetch(`http://localhost:8081/api/tarifas/eliminar/${id}`, {
        method: "DELETE"
      });

      if (!res.ok && res.status !== 204) throw new Error("No se pudo eliminar la tarifa");

      alert("Tarifa eliminada correctamente");
      await fetchTarifas();

    } catch (err) {
      alert("Error al eliminar tarifa: " + err.message);
    }
  }

// Función auxiliar para obtener el ícono según tipo
function obtenerIconoPorTipo(tipo) {
  switch (tipo.toLowerCase()) {
    case "carro": return "🚗";
    case "moto": return "🏍️";
    case "bicicleta": return "🚲";
    default: return "❓";
  }
}

// Renderiza espacios en el contenedor visual principal
function renderizarEspaciosVisuales(espacios) {
  const grid = document.getElementById("grid-espacios");
  if (!grid) {
    console.error("No se encontró el contenedor grid-espacios");
    return;
  }

  grid.innerHTML = "";

  espacios.forEach(e => {
    const div = document.createElement("div");
    div.classList.add("espacio");
    div.classList.add(e.estado.toLowerCase() === "libre" ? "libre" : "ocupado");

    const icono = obtenerIconoPorTipo(e.tipo);

    div.title = `Espacio ${e.numero} - ${e.tipo} - ${e.estado}`;
    div.innerHTML = `
      <span class="icono">${icono}</span>
      <small>${e.numero}</small>
    `;

    grid.appendChild(div);
  });
}

// Lista espacios y los renderiza visualmente
function cargarEspacios() {
  fetch("http://localhost:8081/api/espacios-parqueo/listar")
    .then(res => res.json())
    .then(data => {
      console.log("🔍 Espacios recibidos del backend:", data);
      gridEspacios.innerHTML = "";
      espaciosSeleccionados.clear();
      actualizarEstadoEliminar();
      data.forEach(e => gridEspacios.appendChild(renderizarEspacioConCheckbox(e)));
    })
    .catch(err => console.error("Error al listar espacios:", err));
}

// 🗑️ Evento: eliminar seleccionados
btnEliminarSeleccionados.addEventListener("click", async () => {
  if (espaciosSeleccionados.size === 0) return;

  if (!confirm(`¿Eliminar ${espaciosSeleccionados.size} espacio(s)?`)) return;

  try {
    const res = await fetch("http://localhost:8081/api/espacios-parqueo/eliminar-multiples", {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ids: Array.from(espaciosSeleccionados) })
    });

    if (!res.ok) throw new Error(await res.text());

    alert("Espacios eliminados correctamente");
    cargarEspacios(); // Recargar visualmente
  } catch (err) {
    alert("Error al eliminar espacios: " + err.message);
  }
});

// Enviar formulario para generar espacios
const formGenerarEspacios = document.getElementById("form-generar-espacios");
formGenerarEspacios.addEventListener("submit", async (e) => {
  e.preventDefault();

  const cantidad = parseInt(document.getElementById("cantidad").value);
  const tipo = document.getElementById("tipoEspacio").value;

  if (!cantidad || !tipo) {
    alert("Por favor completa todos los campos.");
    return;
  }

  try {
    const res = await fetch(`http://localhost:8081/api/espacios-parqueo/generar?cantidad=${cantidad}&tipo=${tipo}`, {
      method: "POST"
    });

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(errorText);
    }

    const mensaje = await res.text();
    alert(mensaje);
    formGenerarEspacios.reset();
    await cargarEspacios(); // ✅ Corrección aquí
  } catch (err) {
    console.error("Error al generar espacios: " + err.message);
  }
});

function actualizarEstadoEliminar() {
  const count = espaciosSeleccionados.size;
  contadorSeleccionados.textContent = `${count} seleccionado${count !== 1 ? 's' : ''}`;
  btnEliminarSeleccionados.disabled = count === 0;
}

// Renderiza espacio con checkbox
function renderizarEspacioConCheckbox(espacio) {
  const espacioContainer = document.createElement("div");
  espacioContainer.className = "espacio-item";

  const checkbox = document.createElement("input");
  checkbox.type = "checkbox";
  checkbox.className = "espacio-checkbox";
  checkbox.dataset.id = espacio.id;

  checkbox.addEventListener("change", function () {
    if (this.checked) {
      espaciosSeleccionados.add(espacio.id);
    } else {
      espaciosSeleccionados.delete(espacio.id);
    }
    actualizarEstadoEliminar();
  });

  const btn = document.createElement("button");
  btn.className = `espacio-btn ${espacio.estado.toLowerCase() === "libre" ? "libre" : "ocupado"}`;

  const icono = obtenerIconoPorTipo(espacio.tipo); // ✅ Siempre usamos la misma lógica
  btn.innerHTML = `
    <span class="icono">${icono}</span>
    <small>${espacio.numero}</small>
  `;

  espacioContainer.appendChild(checkbox);
  espacioContainer.appendChild(btn);

  return espacioContainer;
}



  // --- Inicialización ---
fetchVehiculos();
fetchTarifas();
cargarEspacios();

});




















