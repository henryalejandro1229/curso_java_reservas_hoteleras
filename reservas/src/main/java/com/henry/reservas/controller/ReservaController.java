package com.henry.reservas.controller;

import com.henry.commons.controller.CommonController;
import com.henry.commons.dto.reservas.ReservaRequest;
import com.henry.commons.dto.reservas.ReservaResponse;
import com.henry.reservas.service.ReservaService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ReservaController extends CommonController<ReservaRequest, ReservaResponse, ReservaService> {

    public ReservaController(ReservaService service) {
        super(service);
    }

    @GetMapping("/id-habitacion/{id}")
    public ResponseEntity<Void> validarHabitacionDisponible(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id) {
        service.validarHabitacionDisponible(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoReserva(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @PathVariable @Positive(message = "El id de estado debe ser positivo") Long idEstado
    ) {
        service.actualizarEstadoReserva(id, idEstado);
        return ResponseEntity.noContent().build();
    }
}
