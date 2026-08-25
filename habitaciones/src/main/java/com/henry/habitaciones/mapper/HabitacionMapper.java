package com.henry.habitaciones.mapper;

import com.henry.commons.dto.habitaciones.HabitacionRequest;
import com.henry.commons.dto.habitaciones.HabitacionResponse;
import com.henry.commons.enums.EstadoHabitacion;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.mapper.CommonMapper;
import com.henry.habitaciones.entity.Habitacion;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest, HabitacionResponse, Habitacion> {

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        if (request == null) return null;

        return Habitacion.builder()
                .numero(request.numero())
                .capacidad(request.capacidad())
                .precio(request.precio())
                .estadoHabitacion(EstadoHabitacion.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public HabitacionResponse entidadAResponse(Habitacion entidad) {
        if (entidad == null) return null;

        return new HabitacionResponse(
                entidad.getId(),
                entidad.getNumero(),
                entidad.getTipoHabitacion().getDescripcion(),
                entidad.getTipoHabitacion().getCodigo(),
                entidad.getCapacidad(),
                entidad.getPrecio(),
                entidad.getEstadoHabitacion().getDescripcion(),
                entidad.getEstadoHabitacion().getCodigo()
        );
    }
}
