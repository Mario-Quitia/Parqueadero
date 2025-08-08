package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.EspacioParqueo;
import com.example.Parqueadero.entities.RegistroParqueo;
import com.example.Parqueadero.entities.UsuarioSistema;
import com.example.Parqueadero.entities.Vehiculo;
import com.example.Parqueadero.enums.TipoTiempo;
import com.example.Parqueadero.repository.EspacioParqueoRepository;
import com.example.Parqueadero.repository.RegistroParqueoRepository;
import com.example.Parqueadero.repository.UsuarioSistemaRepository;
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
    private final UsuarioSistemaRepository usuarioSistemaRepository;

    @Autowired
    public RegistroParqueoServiceImpl(
            RegistroParqueoRepository registroParqueoRepository,
            EspacioParqueoRepository espacioParqueoRepository,
            VehiculoRepository vehiculoRepository,
            UsuarioSistemaRepository usuarioSistemaRepository
    ) {
        this.registroParqueoRepository = registroParqueoRepository;
        this.espacioParqueoRepository = espacioParqueoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioSistemaRepository = usuarioSistemaRepository;
    }

    @Override
    public RegistroParqueo crearRegistro(RegistroParqueo registro) {
        logger.info("===> Iniciando creación de registro de parqueo...");

        if (registro.getVehiculo() == null || registro.getVehiculo().getPlaca() == null) {
            throw new RuntimeException("Vehículo o placa no proporcionados.");
        }

        String placa = registro.getVehiculo().getPlaca();
        logger.info("===> Placa recibida: {}", placa);
        
        
          // 🔐 VALIDACIÓN: ¿Ya hay un registro activo para esta placa?
    Optional<RegistroParqueo> registroActivo = registroParqueoRepository.findByVehiculo_PlacaAndHoraSalidaIsNull(placa);
    if (registroActivo.isPresent()) {
        throw new RuntimeException("Este vehículo ya tiene un registro activo. No puede ingresar nuevamente.");
    }
        
        
        
        

        Vehiculo vehiculo = vehiculoRepository.findById(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));

        registro.setVehiculo(vehiculo);
        logger.info("===> Vehículo encontrado: {}", vehiculo.getTipo());

        if (registro.getUsuarioRegistro() == null || registro.getUsuarioRegistro().getId() == null) {
            throw new RuntimeException("Usuario es obligatorio para crear el registro.");
        }

        Long usuarioId = registro.getUsuarioRegistro().getId();
        logger.info("===> ID de usuario recibido: {}", usuarioId);

        UsuarioSistema usuario = usuarioSistemaRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        registro.setUsuarioRegistro(usuario);
        logger.info("===> Usuario encontrado: {}", usuario.getNombre());

        String tipoVehiculo = vehiculo.getTipo();
        logger.info("===> Buscando espacio para tipo: {}", tipoVehiculo);

        EspacioParqueo espacioDisponible = espacioParqueoRepository
                .findFirstByTipoAndEstado(tipoVehiculo, "LIBRE")
                .orElseThrow(() -> new RuntimeException("No hay espacios disponibles para tipo: " + tipoVehiculo));

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
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));

        if (registro.getHoraSalida() != null) {
            throw new RuntimeException("Este registro ya fue cerrado.");
        }

        LocalDateTime salida = (horaSalida != null) ? horaSalida : LocalDateTime.now();
        Duration duracion = Duration.between(registro.getHoraEntrada(), salida);

        TipoTiempo tipo = registro.getTarifa().getTipoTiempo();
        logger.info("===> Tipo de tarifa: {}", tipo);
        logger.info("===> Duración calculada (minutos): {}", duracion.toMinutes());

        switch (tipo) {
            case DIA -> {
                if (duracion.toHours() < 24) {
                    throw new RuntimeException("No puede cerrar el registro antes de 24 horas para tarifa diaria.");
                }
            }
            case MES -> {
                if (duracion.toDays() < 30) {
                    throw new RuntimeException("No puede cerrar el registro antes de 30 días para tarifa mensual.");
                }
            }
            default -> {}
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
        logger.info("===> Total a pagar calculado: {}", total);

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

    @Override
    public List<RegistroParqueo> listarActivos() {
        logger.info("===> Listando registros activos (sin hora de salida)");
        return registroParqueoRepository.findByHoraSalidaIsNull();
    }

    @Override
    public List<RegistroParqueo> listarPorPlaca(String placa) {
        logger.info("===> Listando registros por placa: {}", placa);
        return registroParqueoRepository.findByVehiculo_Placa(placa);
    }

    @Override
    public void eliminarRegistro(Long id) {
        logger.info("===> Eliminando registro con ID: {}", id);
        registroParqueoRepository.deleteById(id);
    }
}
