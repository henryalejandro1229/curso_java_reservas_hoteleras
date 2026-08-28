package com.henry.reservas.entity;

import java.time.LocalDateTime;

import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.enums.EstadoReserva;


import com.henry.commons.utils.ValoresNumericosUtils;
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

    public void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalStateException("La reserva ya esta eliminada");
    }

    private void validarEliminacionPermitida() {
        validarNoEliminado();

        if (!estadoReserva.isEliminable())
            throw new IllegalStateException(
                    "La reserva con estado " + estadoReserva
                            + " no puede eliminarse");
    }

    private void validarActualizacionPermitida() {
        validarNoEliminado();

        if (!EstadoReserva.CONFIRMADA.equals(estadoReserva))
            throw new IllegalStateException(
                    "La reserva con estado " + estadoReserva
                            + " no puede actualizarse");
    }

    public void actualizar(
            Long nuevoIdHuesped,
            Long nuevoIdHabitacion,
            LocalDateTime nuevaFechaEntrada,
            LocalDateTime nuevaFechaSalida
    ) {
        validarActualizacionPermitida();
        validarDatos(nuevoIdHuesped, nuevoIdHabitacion);

        if (!nuevaFechaEntrada.isBefore(nuevaFechaSalida))
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la fecha de entrada");

        this.idHuesped = nuevoIdHuesped;
        this.idHabitacion = nuevoIdHabitacion;
        this.fechaEntrada = nuevaFechaEntrada;
        this.fechaSalida = nuevaFechaSalida;
    }

    private static void validarId(Long id, String campo){
        ValoresNumericosUtils.validarLongPositivo(id, "El id del " + campo + " es requerido y debe ser positivo");
    }

    public static void validarDatos(
            Long idHuesped,
            Long  idHabitacion) {
        validarId(idHuesped, "huesped");
        validarId(idHabitacion, "habitaion");
    }

    public void actualizarEstadoReserva(EstadoReserva nuevoEstado) {
        validarNoEliminado();

        if (nuevoEstado == null)
            throw new IllegalArgumentException("El nuevo estado de la reserva es obligatorio");

        if (!estadoReserva.puedeCambiarA(nuevoEstado))
            throw new IllegalStateException("La reserva con estado "
                    + estadoReserva + "no puede cambiar a " + nuevoEstado);

        this.estadoReserva = nuevoEstado;

        if (nuevoEstado == EstadoReserva.CANCELADA){
            this.fechaCancelacion = LocalDateTime.now();
        }
    }

    public void eliminar() {
        validarEliminacionPermitida();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
}
