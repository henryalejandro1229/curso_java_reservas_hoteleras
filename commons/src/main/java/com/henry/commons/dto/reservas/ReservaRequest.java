package com.henry.commons.dto.reservas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservaRequest(

    @NotNull(message = "La fecha de inicio es requerida")
    @Positive(message = "La fecha de inicio debe ser positiva")
    Long idHuesped,

    @NotNull(message = "La fecha de inicio es requerida")
    @Positive(message = "La fecha de inicio debe ser positiva")
    Long idHabitacion
) {
}