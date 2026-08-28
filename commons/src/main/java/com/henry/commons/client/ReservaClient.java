package com.henry.commons.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas")
public interface ReservaClient {

    @GetMapping("/id-habitacion/{id}")
    Void validarHabitacionDisponible(@PathVariable Long id);

    @GetMapping("/huesped/{id}/en-curso")
    Void validarHuespedSinReservaEnCurso(@PathVariable Long id);
}