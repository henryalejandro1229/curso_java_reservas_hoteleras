package com.henry.habitaciones.controller;

import com.henry.commons.controller.CommonController;
import com.henry.commons.dto.habitaciones.HabitacionRequest;
import com.henry.commons.dto.habitaciones.HabitacionResponse;
import com.henry.habitaciones.service.HabitacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {

    public HabitacionController(HabitacionService service) {
        super(service);
    }

    @GetMapping("/id-habitacion/{id}")
    public ResponseEntity<HabitacionResponse> obtenerHabitacionPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(service.obtenerHabitacionPorIdSinEstado(id));
    }

    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoHabitacion(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado
    ) {
        service.actualizarEstadoHabitacion(id, idEstado);
        return ResponseEntity.noContent().build();
    }
}
