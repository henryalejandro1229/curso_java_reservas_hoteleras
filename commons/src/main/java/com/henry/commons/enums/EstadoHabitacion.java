package com.henry.commons.enums;

import com.henry.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum EstadoHabitacion {
    DISPONIBLE(1L, "Lista para asignarse"),
    OCUPADA(2L, "Asignada a una reserva"),
    LIMPIEZA(3L, "En limpieza"),
    MANTENIMIENTO(4L, "En reparación");

    private final Long codigo;

    private final String descripcion;

    public static EstadoHabitacion obtenerEstadoHabitacionPorCodigo(Long codigo) {
        for (EstadoHabitacion e : values()) {
            if(Objects.equals(e.codigo, codigo))
                return e;
        }

        throw new RecursoNoEncontradoException("Estado habitación no válido: " + codigo);
    }
}