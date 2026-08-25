package com.henry.huespedes.controller;

import com.henry.commons.controller.CommonController;
import com.henry.commons.dto.huespedes.HuespedRequest;
import com.henry.commons.dto.huespedes.HuespedResponse;
import com.henry.huespedes.service.HuespedService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class HuespedController extends CommonController<HuespedRequest, HuespedResponse, HuespedService>{

    public HuespedController(HuespedService service) {super(service);}

    @GetMapping("/id-huesped/{id}")
    public ResponseEntity<HuespedResponse> obtenerHuespedPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ){
        return ResponseEntity.ok(service.obtenerHuespedPorIdSinEstado(id));
    }
}
