package com.henry.huespedes.service;

import com.henry.commons.dto.huespedes.HuespedRequest;
import com.henry.commons.dto.huespedes.HuespedResponse;
import com.henry.commons.service.CrudService;

public interface HuespedService extends CrudService<HuespedRequest, HuespedResponse> {
    HuespedResponse obtenerHuespedPorIdSinEstado(Long id);
}
