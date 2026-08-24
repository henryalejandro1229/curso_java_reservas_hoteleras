package com.henry.commons.enums;

import com.henry.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum TipoHabitacion {
    SENCILLA(1L, "Sencilla"),
    DOBLE(2L, "Doble"),
    SUITE(3L, "Suite");

    private final Long codigo;

    private final String descripcion;

    public static TipoHabitacion ObtenerTipoHabitacionPorCodigo(Long codigo) {
        for (TipoHabitacion e : values()) {
            if(Objects.equals(e.codigo, codigo))
                return e;
        }

        throw new RecursoNoEncontradoException("Tipo de habitación no válido: " + codigo);
    }
}
