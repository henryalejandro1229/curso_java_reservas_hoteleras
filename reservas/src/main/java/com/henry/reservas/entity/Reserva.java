package com.henry.reservas.entity;

import java.time.LocalDateTime;

import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.enums.EstadoReserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RESERVAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @Column(name = "ID_HUESPED")
    private Long idHuesped;

    @Column(name = "ID_HABITACION")
    private Long idHabitacion;

    @Column(name = "FECHA_RESERVA")
    private LocalDateTime fechaReserva;

    @Column(name = "FECHA_ENTRADA")
    private LocalDateTime fechaEntrada;

    @Column(name = "FECHA_SALIDA")
    private LocalDateTime fechaSalida;

    @Column(name = "FECHA_CANCELACION")
    private LocalDateTime fechaCancelacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_RESERVA")
    private EstadoReserva estadoReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO")
    private EstadoRegistro estadoRegistro;

    public void setHuesped(Long idHuesped) {
        this.idHuesped = idHuesped;
    }

    public void setHabitacion(Long idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalArgumentException("La reserva ya fue eliminada");
    }

    public void actualizar(Long idHuesped, Long idHabitacion) {
        this.validarNoEliminado();

        this.idHuesped = idHuesped;
        this.idHabitacion = idHabitacion;
    }

    public void actualizarEstadoReserva(EstadoReserva estadoReserva) {
        this.validarNoEliminado();

        if (estadoReserva == null)
            throw new IllegalArgumentException("El estado de reserva es requerido");

        this.estadoReserva = estadoReserva;
    }

    public void eliminar() {
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
}
