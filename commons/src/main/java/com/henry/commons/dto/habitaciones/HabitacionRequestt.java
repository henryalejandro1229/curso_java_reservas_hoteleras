package com.henry.commons.dto.habitaciones;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record HabitacionRequestt(

        @NotNull(message = "El número es requerida")
        @Positive(message = "El número debe ser positivo")
        Short numero,

        @NotNull(message = "El id del tipo habitacion es requerido")
        @Positive(message = "El id del tipo habitacion debe ser positivo")
        Long idTipoHabitacion,

        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad mínima es de 1 persona")
        @Max(value = 10, message = "La capacidad máxima es de 10 personas")
        Short capacidad,

        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser positivo")
        BigDecimal precio
) {
}
