package com.henry.commons.client;

import com.henry.commons.dto.huespedes.HuespedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "huespedes")
public interface HuespedClient {

    @GetMapping("/{id}")
    HuespedResponse obtenerHuespedActivoPorId(@PathVariable Long id);
}
