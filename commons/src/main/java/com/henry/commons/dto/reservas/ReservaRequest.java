package com.henry.commons.dto.reservas;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ReservaRequest(

    @NotNull(message = "La id del huesped es requerida")
    @Positive(message = "El id del huésped debe ser positivo")
    Long idHuesped,

    @NotNull(message = "El id de la habitacion es requerida")
    @Positive(message = "El id del la habitacion debe ser positivo")
    Long idHabitacion,

    @NotNull(message = "La fecha de entrada es requerida")
    @FutureOrPresent(message = "La fecha de entrada debe ser actual o futura")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime fechaEntrada,

    @NotNull(message = "La fecha de la salida es requerida")
    @FutureOrPresent(message = "La fecha de salida debe ser actual a futura")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime fechaSalida
) {
}