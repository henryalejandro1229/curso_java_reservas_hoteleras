package com.henry.reservas.service;

import com.henry.commons.dto.reservas.ReservaRequest;
import com.henry.commons.dto.reservas.ReservaResponse;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.enums.EstadoReserva;
import com.henry.commons.exceptions.RecursoNoEncontradoException;
import com.henry.reservas.entity.Reserva;
import com.henry.reservas.mapper.ReservaMapper;
import com.henry.reservas.repository.ReservaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    private final ReservaMapper reservaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listar() {
        log.info("Listando todas las habitaciones activas");

        return reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(reservaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        log.info("Buscando reserva activa con id: {}", id);

        return reservaMapper.entidadAResponse(obtenerReservaActivaOExcepcion(id));
    }

    @Override
    public ReservaResponse registrar(ReservaRequest request) {
        log.info("Registrando nueva reserva {}", request);

        Reserva reserva = reservaMapper.requestAEntidad(request);
        reserva.setHuesped(request.idHuesped());
        reserva.setHabitacion(request.idHabitacion());

        reservaRepository.save(reserva);

        log.info("Nueva reserva registrada");

        return reservaMapper.entidadAResponse(reserva, null, null);
    }

    @Override
    public ReservaResponse actualizar(ReservaRequest request, long id) {
        log.info("Actualizando reserva con id: {}", id);

        Reserva reserva = obtenerReservaActivaOExcepcion(id);

        //TODO: Valida existencia de huesped y habitacion antes de actualizar

        reserva.actualizar(request.idHuesped(), request.idHabitacion());
        reservaRepository.save(reserva);

        return reservaMapper.entidadAResponse(reserva, null, null);
    }

    @Override
    public void actualizarEstadoReserva(Long idReserva, Long idEstado) {
        log.info("Actualizando estado de reserva con id: {} a estado: {}", idReserva, idEstado);

        Reserva reserva = obtenerReservaActivaOExcepcion(idReserva);

        EstadoReserva nuevoEstado = EstadoReserva.ObtenerEstadoReservaPorCodigo(idEstado);

        reserva.actualizarEstadoReserva(nuevoEstado);

        log.info("Estado de reserva actualizado a: {}", nuevoEstado);
    }

    @Override
    public void eliminar(Long id) {
        Reserva reserva = obtenerReservaActivaOExcepcion(id);

        log.info("Eliminando reserva con id: {}", id);

        reserva.eliminar();

        log.info("Reserva eliminada exitosamente");
    }

    private Reserva obtenerReservaActivaOExcepcion(Long id) {
        return reservaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Reserva activa no encontrada con id: " + id
                ));
    }
}
