
package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.Pago;
import com.example.Parqueadero.entities.RegistroParqueo;
import com.example.Parqueadero.entities.UsuarioSistema;
import com.example.Parqueadero.repository.PagoRepository;
import com.example.Parqueadero.repository.RegistroParqueoRepository;
import com.example.Parqueadero.repository.UsuarioSistemaRepository;
import com.example.Parqueadero.service.PagoService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final RegistroParqueoRepository registroParqueoRepository;
    private final UsuarioSistemaRepository usuarioSistemaRepository;

    public PagoServiceImpl(PagoRepository pagoRepository,
                           RegistroParqueoRepository registroParqueoRepository,
                           UsuarioSistemaRepository usuarioSistemaRepository) {
        this.pagoRepository = pagoRepository;
        this.registroParqueoRepository = registroParqueoRepository;
        this.usuarioSistemaRepository = usuarioSistemaRepository;
    }

    @Override
    public Pago realizarPago(Long idRegistroParqueo, String metodoPago, Long idUsuarioSistema) {
        RegistroParqueo registro = registroParqueoRepository.findById(idRegistroParqueo)
            .orElseThrow(() -> new RuntimeException("Registro de parqueo no encontrado"));

        if (registro.getHoraSalida() == null) {
            registro.setHoraSalida(LocalDateTime.now());
        }

        long minutos = Duration.between(registro.getHoraEntrada(), registro.getHoraSalida()).toMinutes();
        double tarifa = calcularTarifa(minutos);

        UsuarioSistema usuario = usuarioSistemaRepository.findById(idUsuarioSistema)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pago pago = new Pago();
        pago.setRegistroParqueo(registro);
        pago.setFechaPago(LocalDateTime.now());
        pago.setMonto(tarifa);
        pago.setMetodoPago(metodoPago);
        pago.setUsuarioPago(usuario);

        return pagoRepository.save(pago);
    }

    private double calcularTarifa(long minutos) {
        // Tarifa de ejemplo: $500 cada 30 minutos
        return Math.ceil(minutos / 30.0) * 500;
    }

    @Override
    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    @Override
    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("El pago no existe");
        }
        pagoRepository.deleteById(id);
    }
}
