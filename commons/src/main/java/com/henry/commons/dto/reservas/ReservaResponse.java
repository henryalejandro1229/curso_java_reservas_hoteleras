package com.henry.commons.dto.reservas;

import com.henry.commons.dto.habitaciones.DataHabitacion;
import com.henry.commons.dto.huespedes.DataHuesped;

public record ReservaResponse(
    Long id,
    Long idHuesped,
    Long idHabitacion,
    DataHuesped huesped,
    DataHabitacion habitacion,
    String estadoReserva,
    String fechaReserva,
    String fechaEntrada,
    String fechaSalida,
    String fechaCancelacion
) {
}