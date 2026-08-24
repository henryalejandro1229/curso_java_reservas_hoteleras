package com.henry.habitaciones.service;

import com.henry.commons.dto.habitaciones.HabitacionRequestt;
import com.henry.commons.dto.habitaciones.HabitacionResponse;
import com.henry.commons.service.CrudService;

public interface HabitacionService extends CrudService<HabitacionRequestt, HabitacionResponse> {

    HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id);

    void actualizarEstadoHabitacion(Long idHabitacion, Long idEstado);
}
