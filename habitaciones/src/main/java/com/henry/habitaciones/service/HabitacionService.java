package com.henry.habitaciones.service;

import com.henry.commons.dto.habitaciones.HabitacionRequest;
import com.henry.commons.dto.habitaciones.HabitacionResponse;
import com.henry.commons.service.CrudService;

import java.util.List;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

    HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id);

    List<HabitacionResponse> listar(Boolean disponibles);

    void actualizarEstadoHabitacion(Long idHabitacion, Long idEstado);
}
