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
import com.henry.commons.exceptions.EntidadRelacionadaException;
import com.henry.commons.exceptions.RecursoNoEncontradoException;
import com.henry.reservas.entity.Reserva;
import com.henry.reservas.mapper.ReservaMapper;
import com.henry.reservas.repository.ReservaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        log.info("Listando todas las reservas activas");

        List<Reserva> reservas = reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO);
        Map<Long, HuespedResponse> huespedesPorId = new HashMap<>();
        Map<Long, HabitacionResponse> habitacionesPorId = new HashMap<>();

        return reservas.stream()
                .map(reserva -> {
                    HuespedResponse huespedResponse = huespedesPorId.computeIfAbsent(
                            reserva.getIdHuesped(),
                            this::obtenerHuespedPorIdSinEstado
                    );
                    HabitacionResponse habitacionResponse = habitacionesPorId.computeIfAbsent(
                            reserva.getIdHabitacion(),
                            this::obtenerHabitacionPorIdSinEstado
                    );

                    return reservaMapper.entidadAResponse(
                            reserva,
                            huespedResponse,
                            habitacionResponse
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        log.info("Buscando reserva activa con id: {}", id);
        Reserva reserva = obtenerReservaActivaOExcepcion(id);
        HuespedResponse huespedResponse = obtenerHuespedPorIdSinEstado(reserva.getIdHuesped());
        HabitacionResponse habitacionResponse = obtenerHabitacionPorIdSinEstado(reserva.getIdHabitacion());
        return reservaMapper.entidadAResponse(
                reserva,
                huespedResponse,
                habitacionResponse
        );
    }

    @Override
    public ReservaResponse registrar(ReservaRequest request) {
        log.info("Registrando nueva reserva {}", request);

        validaFechaEntradaSalida(request);

        HuespedResponse huespedResponse = validarHuespedDisponible(request.idHuesped());

        HabitacionResponse habitacionResponse = obtenerHabitacionActivaPorId(request.idHabitacion());

        validarEstatusDisponibleHabitacion(habitacionResponse.idEstadoHabitacion());

        Reserva reserva = reservaMapper.requestAEntidad(request);

        reservaRepository.save(reserva);

        actualizarEstadoHabitacion(habitacionResponse.id(), EstadoHabitacion.OCUPADA.getCodigo());

        log.info("Nueva reserva registrada");

        return reservaMapper.entidadAResponse(reserva, huespedResponse, habitacionResponse);
    }

    @Override
    public ReservaResponse actualizar(ReservaRequest request, Long id) {
        log.info("Actualizando reserva con id: {}", id);

        Reserva reserva = obtenerReservaActivaOExcepcion(id);

        reserva.validarActualizacionPermitida();

        validaFechaEntradaSalida(request);

        HuespedResponse huespedResponse = obtenerHuespedPorIdSinEstado(reserva.getIdHuesped());

        HabitacionResponse habitacionResponse = obtenerHabitacionActivaPorId(reserva.getIdHabitacion());

        boolean esNuevaHabitacion = !reserva.getIdHabitacion().equals(request.idHabitacion());

        if (esNuevaHabitacion) validarEstatusDisponibleHabitacion(habitacionResponse.idEstadoHabitacion());

        reserva.actualizar(
                request.idHuesped(),
                request.idHabitacion(),
                request.fechaEntrada(),
                request.fechaSalida()
        );

        if (esNuevaHabitacion) {
            actualizarEstadoHabitacion(habitacionResponse.id(), EstadoHabitacion.OCUPADA.getCodigo());
            actualizarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getCodigo());
        }

        log.info("Reserva con id {} actualizada correctamente", id);

        return reservaMapper.entidadAResponse(
                reserva,
                huespedResponse,
                habitacionResponse);
    }

    @Override
    public void actualizarEstadoReserva(Long idReserva, Long idEstado) {
        log.info("Actualizando estado de reserva con id: {} a estado: {}", idReserva, idEstado);

        Reserva reserva = obtenerReservaActivaOExcepcion(idReserva);

        reserva.actualizarEstadoReserva(EstadoReserva.obtenerEstadoReservaPorCodigo(idEstado));

        actualizarEstadoHabitacion(reserva.getIdHabitacion(), obtenerNuevoEstadoHabitacion(idEstado));
    }

    @Override
    public void eliminar(Long id) {
        Reserva reserva = obtenerReservaActivaOExcepcion(id);

        log.info("Eliminando reserva con id: {}", id);

        reserva.eliminar();

        if (EstadoReserva.CONFIRMADA.equals(reserva.getEstadoReserva())) {
            actualizarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getCodigo());
        }

        log.info("Reserva eliminada exitosamente");
    }

    private Reserva obtenerReservaActivaOExcepcion(Long id) {
        return reservaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Reserva activa no encontrada con id: " + id
                ));
    }

    private void validarEstatusDisponibleHabitacion(Long idEstatus) {
        log.info("Estatus de habitacion {} ", EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstatus));
        if (!EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstatus).equals(EstadoHabitacion.DISPONIBLE)) {
            throw new IllegalStateException("La habitación no tiene un estado disponible para el registro de la reserva");
        }
    }

    private static final List<EstadoReserva> ESTADO_ACTIVOS =
            List.of(
                    EstadoReserva.EN_CURSO
            );

    private HuespedResponse validarHuespedDisponible(Long idHuesped){
        HuespedResponse huesped = huespedClient.obtenerHuespedActivoPorId(idHuesped);

        boolean tieneReservaActiva = reservaRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
                idHuesped,
                EstadoRegistro.ACTIVO,
                ESTADO_ACTIVOS

        );

        if(tieneReservaActiva)
            throw new EntidadRelacionadaException("El huesped ya tiene una reserva en curso");

        return huesped;
    }

    private Long obtenerNuevoEstadoHabitacion(Long idEstadoReserva){
        EstadoReserva estadoReserva = EstadoReserva.obtenerEstadoReservaPorCodigo(idEstadoReserva);
        if (EstadoReserva.FINALIZADA.equals(estadoReserva) || EstadoReserva.CANCELADA.equals(estadoReserva)) {
            return EstadoHabitacion.DISPONIBLE.getCodigo();
        }
        return EstadoHabitacion.OCUPADA.getCodigo();
    }

    //Consumo de APIs servicio de habitaciones

    private HabitacionResponse obtenerHabitacionActivaPorId(Long id) {
        log.info("Buscando habitacion con id {} en el servicio remoto...", id);

        return habitacionClient.obtenerHabitacionActivaPorId(id);
    }

    private HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id){
        log.info("Buscando habitacion con id {} en el servicio remoto...", id);
        return habitacionClient.obtenerHabitacionPorIdSinEstado(id);
    }

    private void actualizarEstadoHabitacion(Long id, Long idHabitacion) {
        log.info("Actualizando habitacion con id {} en el servicio remoto...", id);

        habitacionClient.actualizarEstadoHabitacion(id, idHabitacion);
    };

    //Consumo de APIs servicio de huespedes

    private HuespedResponse obtenerHuespedActivoPorId(Long id) {
        log.info("Buscando huesped activo con id {} en el servicio remoto...", id);

        return huespedClient.obtenerHuespedActivoPorId(id);
    }

    private HuespedResponse obtenerHuespedPorIdSinEstado(Long id){
        log.info("Buscando huesped con id {} en el servicio remoto...", id);
        return huespedClient.obtenerHuespedPorIdSinEstado(id);
    }

    private void validaFechaEntradaSalida(ReservaRequest request) {
        log.info("Validando fecha entrada y fecha salida");

        if (!request.fechaEntrada().isBefore(request.fechaSalida())) {
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la fecha de entrada"
            );
        }
    }
}
