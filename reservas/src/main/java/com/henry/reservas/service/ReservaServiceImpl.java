package com.henry.reservas.service;

import com.henry.commons.client.HabitacionClient;
import com.henry.commons.client.HuespedClient;
import com.henry.commons.dto.habitaciones.HabitacionResponse;
import com.henry.commons.dto.huespedes.HuespedResponse;
import com.henry.commons.dto.reservas.ReservaRequest;
import com.henry.commons.dto.reservas.ReservaResponse;
import com.henry.commons.enums.EstadoHabitacion;
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

    private final HuespedClient huespedClient;

    private final HabitacionClient habitacionClient;

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

        HuespedResponse huespedResponse = obtenerHuespedActivoPorId(request.idHuesped());

        HabitacionResponse habitacionResponse = obtenerHabitacionActivaPorId(request.idHabitacion());

        validarEstatusDisponibleHabitacion(habitacionResponse.idEstadoHabitacion());

        Reserva reserva = reservaMapper.requestAEntidad(request);

        reservaRepository.save(reserva);

        actualizaHabitacionActivaPorId(habitacionResponse.id(), EstadoHabitacion.OCUPADA.getCodigo());

        log.info("Nueva reserva registrada");

        return reservaMapper.entidadAResponse(reserva, huespedResponse, habitacionResponse);
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

    private void validarEstatusDisponibleHabitacion(Long idEstatus) {
        if (!EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstatus).equals(EstadoHabitacion.DISPONIBLE)) {
            throw new IllegalStateException("La habitación no tiene un estado disponible para el registro de la reserva");
        }
    }

    //Consumo de APIs servicio de habitaciones

    private HabitacionResponse obtenerHabitacionActivaPorId(Long id) {
        log.info("Buscando habitacion con id {} en el servicio remoto...", id);

        return habitacionClient.obtenerHabitacionActivaPorId(id);
    }

    private void actualizaHabitacionActivaPorId(Long id, Long idHabitacion) {
        log.info("Actualizando habitacion con id {} en el servicio remoto...", id);

        habitacionClient.actualizarEstadoHabitacion(id, idHabitacion);
    };

    //Consumo de APIs servicio de huespedes

    private HuespedResponse obtenerHuespedActivoPorId(Long id) {
        log.info("Buscando huesped con id {} en el servicio remoto...", id);

        return huespedClient.obtenerHuespedActivoPorId(id);
    }
}
