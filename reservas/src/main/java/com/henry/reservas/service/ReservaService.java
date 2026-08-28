package com.henry.reservas.service;

import com.henry.commons.dto.reservas.ReservaRequest;
import com.henry.commons.dto.reservas.ReservaResponse;
import com.henry.commons.service.CrudService;

public interface ReservaService extends CrudService<ReservaRequest,ReservaResponse> {

    void actualizarEstadoReserva(Long idReserva, Long idEstado);

    void validarHabitacionDisponible(Long idHabitacion);

    void validarHuespedSinReservaEnCurso(Long idHuesped);

}
