package com.henry.commons.dto.habitaciones;

import java.math.BigDecimal;

public record DataHabitacion(
        Short numero,
        String tipo,
        Short capacidad,
        BigDecimal precio,
        String estadoHabitacion
) {
}
