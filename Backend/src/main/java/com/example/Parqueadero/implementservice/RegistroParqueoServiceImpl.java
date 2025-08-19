package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.EspacioParqueo;
import com.example.Parqueadero.entities.RegistroParqueo;
import com.example.Parqueadero.entities.Vehiculo;
import com.example.Parqueadero.enums.TipoTiempo;
import com.example.Parqueadero.repository.EspacioParqueoRepository;
import com.example.Parqueadero.repository.RegistroParqueoRepository;
import com.example.Parqueadero.repository.VehiculoRepository;
import com.example.Parqueadero.service.RegistroParqueoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistroParqueoServiceImpl implements RegistroParqueoService {

    private static final Logger logger = LoggerFactory.getLogger(RegistroParqueoServiceImpl.class);

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
        logger.info("===> Iniciando creación de registro de parqueo...");

        if (registro.getVehiculo() == null || registro.getVehiculo().getPlaca() == null) {
            throw new IllegalArgumentException("Vehículo o placa no proporcionados.");
        }

        String placa = registro.getVehiculo().getPlaca();
        logger.info("===> Placa recibida: {}", placa);

        // Validación: ¿Ya hay un registro activo para esta placa?
        Optional<RegistroParqueo> registroActivo =
                registroParqueoRepository.findByVehiculo_PlacaAndHoraSalidaIsNull(placa);

        if (registroActivo.isPresent()) {
            throw new IllegalStateException("Este vehículo ya tiene un registro activo. No puede ingresar nuevamente.");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(placa)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con placa: " + placa));

        registro.setVehiculo(vehiculo);
        logger.info("===> Vehículo encontrado: {}", vehiculo.getTipo());

        // Buscar espacio disponible
        EspacioParqueo espacioDisponible = espacioParqueoRepository
                .findFirstByTipoAndEstado(vehiculo.getTipo(), "LIBRE")
                .orElseThrow(() -> new IllegalStateException(
                        "No hay espacios disponibles para tipo: " + vehiculo.getTipo()
                ));

        espacioDisponible.setEstado("OCUPADO");
        espacioParqueoRepository.save(espacioDisponible);

        registro.setEspacioParqueo(espacioDisponible);
        registro.setHoraEntrada(LocalDateTime.now());

        logger.info("===> Espacio asignado: {}", espacioDisponible.getNumero());
        logger.info("===> Hora de entrada: {}", registro.getHoraEntrada());

        RegistroParqueo nuevoRegistro = registroParqueoRepository.save(registro);
        logger.info("===> Registro creado exitosamente con ID: {}", nuevoRegistro.getId());

        return nuevoRegistro;
    }

    @Override
    public RegistroParqueo cerrarRegistro(Long id, LocalDateTime horaSalida) {
        logger.info("===> Cerrando registro con ID: {}", id);

        RegistroParqueo registro = registroParqueoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado con id: " + id));

        if (registro.getHoraSalida() != null) {
            throw new IllegalStateException("Este registro ya fue cerrado.");
        }

        LocalDateTime salida = (horaSalida != null) ? horaSalida : LocalDateTime.now();
        Duration duracion = Duration.between(registro.getHoraEntrada(), salida);

        TipoTiempo tipo = registro.getTarifa().getTipoTiempo();
        logger.info("===> Tipo de tarifa: {}", tipo);
        logger.info("===> Duración calculada (minutos): {}", duracion.toMinutes());

        // Validaciones de tiempo mínimo según tarifa
        switch (tipo) {
            case DIA -> {
                if (duracion.toHours() < 24) {
                    throw new IllegalStateException("No puede cerrar el registro antes de 24 horas para tarifa diaria.");
                }
            }
            case MES -> {
                if (duracion.toDays() < 30) {
                    throw new IllegalStateException("No puede cerrar el registro antes de 30 días para tarifa mensual.");
                }
            }
            default -> {}
        }

        registro.setHoraSalida(salida);

        // Cálculo del total a pagar
        long minutos = duracion.toMinutes();
        double total = switch (tipo) {
            case HORA -> Math.ceil(minutos / 60.0) * registro.getTarifa().getValorHora().doubleValue();
            case DIA -> {
                long dias = Math.max(1, duracion.toDays());
                yield registro.getTarifa().getValor() * dias;
            }
            case MES -> {
                long diasTotal = duracion.toDays();
                long meses = Math.max(1, (long) Math.ceil(diasTotal / 30.0));
                yield registro.getTarifa().getValor() * meses;
            }
        };

        registro.setTotalPagado(total);
        logger.info("===> Total a pagar calculado: {}", total);

        // Liberar espacio
        EspacioParqueo espacio = registro.getEspacioParqueo();
        espacio.setEstado("LIBRE");
        espacioParqueoRepository.save(espacio);
        logger.info("===> Espacio liberado: {}", espacio.getNumero());

        RegistroParqueo registroCerrado = registroParqueoRepository.save(registro);
        logger.info("===> Registro cerrado con éxito. ID: {}", registroCerrado.getId());

        return registroCerrado;
    }

    @Override
    public Optional<RegistroParqueo> obtenerPorId(Long id) {
        logger.info("===> Buscando registro por ID: {}", id);
        return registroParqueoRepository.findById(id);
    }

    @Override
    public List<RegistroParqueo> listarTodos() {
        logger.info("===> Listando todos los registros de parqueo");
        return registroParqueoRepository.findAll();
    }



   
}
