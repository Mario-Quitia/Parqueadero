package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.EspacioParqueo;
import com.example.Parqueadero.entities.RegistroParqueo;
import com.example.Parqueadero.entities.Vehiculo;
import com.example.Parqueadero.enums.TipoTiempo;
import com.example.Parqueadero.repository.EspacioParqueoRepository;
import com.example.Parqueadero.repository.RegistroParqueoRepository;
import com.example.Parqueadero.repository.VehiculoRepository;
import com.example.Parqueadero.service.RegistroParqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistroParqueoServiceImpl implements RegistroParqueoService {

    private final RegistroParqueoRepository registroParqueoRepository;
    private final EspacioParqueoRepository espacioParqueoRepository;
    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public RegistroParqueoServiceImpl(
        RegistroParqueoRepository registroParqueoRepository,
        EspacioParqueoRepository espacioParqueoRepository,
        VehiculoRepository vehiculoRepository
    ) {
        this.registroParqueoRepository = registroParqueoRepository;
        this.espacioParqueoRepository = espacioParqueoRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public RegistroParqueo crearRegistro(RegistroParqueo registro) {
        String placa = registro.getVehiculo().getPlaca();

        // Buscar el vehículo completo en la base de datos
        Vehiculo vehiculo = vehiculoRepository.findById(placa)
            .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));

        registro.setVehiculo(vehiculo);

        String tipoVehiculo = vehiculo.getTipo();

        EspacioParqueo espacioDisponible = espacioParqueoRepository
            .findFirstByTipoAndEstado(tipoVehiculo, "LIBRE")
            .orElseThrow(() -> new RuntimeException("No hay espacios disponibles para tipo: " + tipoVehiculo));

        espacioDisponible.setEstado("OCUPADO");
        espacioParqueoRepository.save(espacioDisponible);

        registro.setEspacioParqueo(espacioDisponible);
        registro.setHoraEntrada(LocalDateTime.now());

        return registroParqueoRepository.save(registro);
    }

    @Override
public RegistroParqueo cerrarRegistro(Long id, LocalDateTime horaSalida) {
    RegistroParqueo registro = registroParqueoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));

    if (registro.getHoraSalida() != null) {
        throw new RuntimeException("Este registro ya fue cerrado.");
    }

    LocalDateTime salida = (horaSalida != null) ? horaSalida : LocalDateTime.now();

    // Calcular duración entre entrada y salida propuestas
    Duration duracion = Duration.between(registro.getHoraEntrada(), salida);

    // Obtener tipo de tarifa
    TipoTiempo tipo = registro.getTarifa().getTipoTiempo();

    // Validaciones según tipo de tarifa
    switch (tipo) {
        case DIA:
            if (duracion.toHours() < 24) {
                throw new RuntimeException("No puede cerrar el registro antes de 24 horas para tarifa diaria.");
            }
            break;
        case MES:
            if (duracion.toDays() < 30) {
                throw new RuntimeException("No puede cerrar el registro antes de 30 días para tarifa mensual.");
            }
            break;
        default:
            // No restricción para tarifa por hora (u otro tipo)
            break;
    }

    registro.setHoraSalida(salida);

    long minutos = duracion.toMinutes();

    double total = switch (tipo) {
        case HORA -> Math.ceil(minutos / 60.0) * registro.getTarifa().getValorHora().doubleValue();
        case DIA -> {
            long dias = duracion.toDays();
            yield registro.getTarifa().getValor() * (dias == 0 ? 1 : dias);
        }
        case MES -> {
            long diasTotal = duracion.toDays();
            long meses = (long) Math.ceil(diasTotal / 30.0);
            yield registro.getTarifa().getValor() * (meses == 0 ? 1 : meses);
        }
    };

    registro.setTotalPagado(total);

    // Liberar espacio de parqueo
    EspacioParqueo espacio = registro.getEspacioParqueo();
    espacio.setEstado("LIBRE");
    espacioParqueoRepository.save(espacio);

    return registroParqueoRepository.save(registro);
}

    @Override
    public Optional<RegistroParqueo> obtenerPorId(Long id) {
        return registroParqueoRepository.findById(id);
    }

    @Override
    public List<RegistroParqueo> listarTodos() {
        return registroParqueoRepository.findAll();
    }

    @Override
    public List<RegistroParqueo> listarActivos() {
        return registroParqueoRepository.findByHoraSalidaIsNull();
    }

    @Override
    public List<RegistroParqueo> listarPorPlaca(String placa) {
        return registroParqueoRepository.findByVehiculo_Placa(placa);
    }

    @Override
    public void eliminarRegistro(Long id) {
        registroParqueoRepository.deleteById(id);
    }
}
