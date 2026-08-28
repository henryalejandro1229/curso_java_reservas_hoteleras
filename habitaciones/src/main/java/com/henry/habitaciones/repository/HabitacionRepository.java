package com.henry.habitaciones.repository;

import com.henry.commons.enums.EstadoHabitacion;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.habitaciones.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    List<Habitacion> findByEstadoRegistroAndEstadoHabitacion(
        EstadoRegistro estadoRegistro,
        EstadoHabitacion estadoHabitacion
    );

    Optional<Habitacion> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistro(Short numero, EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistroAndIdNot(Short numero, EstadoRegistro estadoRegistro, Long id);
}
