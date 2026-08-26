package com.henry.commons.client;

import com.henry.commons.dto.habitaciones.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "habitaciones")
public interface HabitacionClient {

    @GetMapping("/{id}")
    HabitacionResponse obtenerHabitacionActivaPorId(@PathVariable Long id);

    @PutMapping("/{id}/estado/{idEstado}")
    Void actualizarEstadoHabitacion(
            @PathVariable Long id,
            @PathVariable Long idEstado
    );
}
