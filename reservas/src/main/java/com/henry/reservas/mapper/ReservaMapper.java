package com.henry.reservas.mapper;

import com.henry.commons.dto.habitaciones.DataHabitacion;
import com.henry.commons.dto.habitaciones.HabitacionResponse;
import com.henry.commons.dto.huespedes.DataHuesped;
import com.henry.commons.dto.huespedes.HuespedResponse;
import com.henry.commons.dto.reservas.ReservaRequest;
import com.henry.commons.dto.reservas.ReservaResponse;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.enums.EstadoReserva;
import com.henry.commons.mapper.CommonMapper;
import com.henry.commons.utils.StringCustomUtils;
import com.henry.reservas.entity.Reserva;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservaMapper implements CommonMapper<ReservaRequest, ReservaResponse, Reserva> {

    @Override
    public Reserva requestAEntidad(ReservaRequest request) {
        if (request == null) return null;

        return Reserva.builder()
                .idHuesped(request.idHuesped())
                .idHabitacion(request.idHabitacion())
                .estadoReserva(EstadoReserva.CONFIRMADA)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .fechaReserva(LocalDateTime.now())
                .build();
    }

    @Override
    public ReservaResponse entidadAResponse(Reserva entidad) {
        if (entidad == null) return null;

        return new ReservaResponse(
                entidad.getId(),
                null,
                null,
                entidad.getEstadoReserva().name(),
                StringCustomUtils.localDateTimeAString(entidad.getFechaReserva()),
                StringCustomUtils.localDateTimeAString(entidad.getFechaEntrada()),
                StringCustomUtils.localDateTimeAString(entidad.getFechaSalida()),
                StringCustomUtils.localDateTimeAString(entidad.getFechaCancelacion())
        );
    }

    public ReservaResponse entidadAResponse(Reserva entidad, HuespedResponse huespedResponse, HabitacionResponse habitacionResponse) {
        if (entidad == null) return null;

        return new ReservaResponse(
                entidad.getId(),
                huespedResponseADatosHuesped(huespedResponse),
                habitacionResponseADatosHabitacion(habitacionResponse),
                entidad.getEstadoReserva().name(),
                StringCustomUtils.localDateTimeAString(entidad.getFechaReserva()),
                StringCustomUtils.localDateTimeAString(entidad.getFechaEntrada()),
                StringCustomUtils.localDateTimeAString(entidad.getFechaSalida()),
                entidad.getFechaCancelacion() != null ? StringCustomUtils.localDateTimeAString(entidad.getFechaCancelacion()) : null
        );
    }

    public DataHuesped huespedResponseADatosHuesped(HuespedResponse huespedResponse) {
        if (huespedResponse == null) return null;

        return new DataHuesped(
                huespedResponse.nombre(),
                huespedResponse.email(),
                huespedResponse.telefono()
        );
    }

    public DataHabitacion habitacionResponseADatosHabitacion(HabitacionResponse habitacionResponse) {
        if (habitacionResponse == null) return null;

        return new DataHabitacion(
                habitacionResponse.numero(),
                habitacionResponse.tipo(),
                habitacionResponse.capacidad(),
                habitacionResponse.precio(),
                habitacionResponse.estadoHabitacion()
        );
    }

}
