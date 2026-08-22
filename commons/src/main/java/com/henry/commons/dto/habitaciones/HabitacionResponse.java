package com.henry.commons.dto.habitaciones;

import java.math.BigDecimal;

public record HabitacionResponse(
        Long id,
        Short numero,
        String tipo,
        Long idTipo,
        Short capacidad,
        BigDecimal precio,
        String estadoHabitacion,
        Long idEstadoHabitacion
) {
}
