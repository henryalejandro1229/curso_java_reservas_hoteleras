package com.henry.commons.enums;

import com.henry.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum EstadoReserva {
    CONFIRMADA(1L, "Reserva creada"),
    EN_CURSO(2L, "Check-in realizado"),
    FINALIZADA(3L, "Check-out realizado"),
    CANCELADA(4L, "Reserva cancelada");

    private final Long codigo;

    private final String descripcion;

    public static EstadoReserva ObtenerEstadoReservaPorCodigo(Long codigo) {
        for (EstadoReserva e : values()) {
            if(Objects.equals(e.codigo, codigo))
                return e;
        }

        throw new RecursoNoEncontradoException("Estado reserva no válido: " + codigo);
    }
}