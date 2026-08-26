package com.henry.huespedes.mapper;

import com.henry.commons.dto.huespedes.HuespedRequest;
import com.henry.commons.dto.huespedes.HuespedResponse;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.mapper.CommonMapper;
import com.henry.huespedes.entity.Huesped;
import org.springframework.stereotype.Component;

@Component
public class HuespedMapper implements CommonMapper<HuespedRequest, HuespedResponse, Huesped> {

    @Override
    public Huesped requestAEntidad(HuespedRequest request) {
        if (request == null) return null;
        return Huesped.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .nacionalidad(request.nacionalidad().trim())
                .email(request.email().trim())
                .telefono(request.telefono().trim())
                .documento(request.documento().trim())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public HuespedResponse entidadAResponse(Huesped entidad) {
        if (entidad == null) return null;
        return new HuespedResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getNacionalidad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDocumento()
        );
    }
}
