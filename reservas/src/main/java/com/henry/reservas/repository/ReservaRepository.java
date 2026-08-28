package com.henry.reservas.repository;

import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.enums.EstadoReserva;
import com.henry.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(Long idHuesped, EstadoRegistro estadoRegistro, List<EstadoReserva> estados);

    boolean existsByIdHabitacionAndEstadoReservaInAndIdNot(Long idHabitacion, List<EstadoReserva> estadoReserva, Long idReserva);
}
