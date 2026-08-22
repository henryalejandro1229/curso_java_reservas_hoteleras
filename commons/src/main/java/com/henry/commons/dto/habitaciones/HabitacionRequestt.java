package com.henry.commons.dto.habitaciones;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record HabitacionRequestt(

        @NotNull(message = "La edad es requerida")
        @Min(value = 18, message = "La edad minima es de 18 años")
        @Max(value = 100, message = "La edad maxima es de 100 años")
        Short numero,

        @NotNull(message = "El id del tipo habitacion es requerido")
        @Positive(message = "El id del tipo habitacion debe ser positivo")
        Long idTipoHabitacion,

        @NotNull(message = "La capacidad es requerida")
        @Min(value = 18, message = "La capacidad minima es de 18 años")
        @Max(value = 100, message = "La capacidad maxima es de 100 años")
        Short capacidad,

        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser positivo")
        BigDecimal precio
) {
}
