document.addEventListener("DOMContentLoaded", () => {
  cargarEspaciosDisponibles();
  cargarTarifasActivas();
  cargarUsuarios();
  
  const formRegistro = document.getElementById("form-registro-parqueo");
  formRegistro.addEventListener("submit", registrarParqueo);
});

// 👉 Cargar espacios disponibles
function cargarEspaciosDisponibles() {
  fetch("http://localhost:8081/api/espacios-parqueo/listar")
    .then(response => response.json())
    .then(data => {
      const select = document.getElementById("espacioEntrada");
      select.innerHTML = '<option value="">-- Selecciona un espacio --</option>';
      data.forEach(espacio => {
        const option = document.createElement("option");
        option.value = espacio.id;
        option.textContent = `${espacio.numero} - ${espacio.tipo}`;
        select.appendChild(option);
      });
    })
    .catch(error => console.error("Error cargando espacios:", error));
}

// 👉 Cargar tarifas activas
function cargarTarifasActivas() {
  fetch("http://localhost:8081/api/tarifas/listar")
    .then(response => response.json())
    .then(data => {
      const select = document.getElementById("tarifaEntrada");
      select.innerHTML = '<option value="">-- Selecciona una tarifa --</option>';
      data.forEach(tarifa => {
        const option = document.createElement("option");
        option.value = tarifa.id;
        option.textContent = `${tarifa.tipoVehiculo} - ${tarifa.tipoTiempo} ($${tarifa.valor})`;
        select.appendChild(option);
      });
    })
    .catch(error => console.error("Error cargando tarifas:", error));
}

// 👉 Cargar usuarios
function cargarUsuarios() {
  fetch("http://localhost:8081/api/usuarios")
    .then(response => response.json())
    .then(data => {
      const select = document.getElementById("usuarioEntrada");
      select.innerHTML = '<option value="">-- Selecciona un usuario --</option>';
      data.forEach(usuario => {
        const option = document.createElement("option");
        option.value = usuario.id;
        option.textContent = `${usuario.nombre} (${usuario.rol})`;
        select.appendChild(option);
      });
    })
    .catch(error => console.error("Error cargando usuarios:", error));
}






cargarPlacas();

  // Cuando se selecciona una placa del datalist
  document.getElementById('placaEntrada').addEventListener('change', async (e) => {
    const placa = e.target.value.trim().toUpperCase();
    if (!placa) return;

    try {
      const response = await fetch(`http://localhost:8081/api/vehiculos/obtener/${placa}`);
      if (!response.ok) throw new Error('Vehículo no encontrado');
      const vehiculo = await response.json();
      console.log("🚗 Vehículo encontrado:", vehiculo);

      // Aquí podrías usar el tipo si lo necesitas
      // Por ejemplo: mostrarlo, validarlo o pasarlo como dato oculto
      // document.getElementById("tipoVehiculo").value = vehiculo.tipo;

    } catch (error) {
      alert(`Error al buscar placa ${placa}: ${error.message}`);
    }
  });


async function cargarPlacas() {
  try {
    const response = await fetch("http://localhost:8081/api/vehiculos/listar");
    if (!response.ok) throw new Error("Error al cargar placas");

    const vehiculos = await response.json();
    const datalist = document.getElementById("listaPlacas");
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


function registrarParqueo(event) {
  event.preventDefault();

  const form = document.getElementById("form-registro-parqueo");
  const mensaje = document.getElementById("mensajeEntrada");

  const placa = document.getElementById("placaEntrada").value.trim().toUpperCase();
  const espacioId = parseInt(document.getElementById("espacioEntrada").value);
  const tarifaId = parseInt(document.getElementById("tarifaEntrada").value);

  // Validación básica
  if (!placa || isNaN(espacioId) || isNaN(tarifaId) ) {
    mensaje.textContent = "❌ Todos los campos son obligatorios y deben ser válidos.";
    mensaje.style.color = "red";
    alert("❌ Todos los campos son obligatorios y deben ser válidos.");
    return;
  }

  const data = {
    vehiculo: { placa: placa },
    espacioParqueo: { id: espacioId },
    tarifa: { id: tarifaId }
    
  };

  fetch("http://localhost:8081/api/registros/crear", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  })
    .then(response => {
      if (!response.ok) {
        return response.text().then(text => {
          try {
            const json = JSON.parse(text);
            throw new Error(json.message || json.error || "Error desconocido del servidor");
          } catch {
            throw new Error(text || "Error desconocido del servidor");
          }
        });
      }
      return response.json();
    })
    .then(resultado => {
      mensaje.textContent = "✅ Entrada registrada con éxito.";
      mensaje.style.color = "green";
      alert("✅ Entrada registrada con éxito.");
      document.getElementById("form-registro-parqueo").reset();
      cargarEspaciosDisponibles();
    })
    .catch(error => {
      console.error("❌ Error al registrar entrada:", error);
      mensaje.textContent = `❌ No se pudo registrar la entrada: `;
      mensaje.style.color = "red";
      alert(`El vehículo ya se encuentra parqueado o el espacio seleccionado está ocupado. 
        Por favor, verifica la información e intenta nuevamente`);
    });
}








