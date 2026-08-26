package com.henry.habitaciones.entity;

import com.henry.commons.enums.EstadoHabitacion;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.enums.TipoHabitacion;
import com.henry.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "HABITACIONES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long id;

    @Column(name = "NUMERO", nullable = false)
    private Short numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO", nullable = false)
    private TipoHabitacion tipoHabitacion;

    @Column(name = "CAPACIDAD", nullable = false)
    private Short capacidad;

    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HABITACION", nullable = false)
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public void validarDatos(Short numero, TipoHabitacion tipoHabitacion, Short capacidad, BigDecimal precio) {
        ValoresNumericosUtils.validarRangoShort(
                numero, (short) 1, Short.MAX_VALUE, "El numero de habitación es requerido y debe ser positivo");
        ValoresNumericosUtils.validarRangoShort(
                capacidad, (short) 1, Short.MAX_VALUE, "La capacidad es requerida y debe ser positiva");
        ValoresNumericosUtils.validarBigDecimalPositivo(
                precio, "El precio es requerido y debe ser positivo");

        if (tipoHabitacion == null)
            throw new IllegalArgumentException("El tipo de habitación es requerido");
    }

    private void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalStateException("La habitación ya esta eliminada");
    }

    public void actualizarTipoHabitacion(TipoHabitacion tipoHabitacion) {
        validarNoEliminado();

        if (tipoHabitacion == null)
            throw new IllegalArgumentException("El tipo de habitación es requerido");

        this.tipoHabitacion = tipoHabitacion;
    }

    public void actualizarEstadoHabitacion(EstadoHabitacion estadoHabitacion) {
        validarNoEliminado();

        if (estadoHabitacion == null)
            throw new IllegalArgumentException("El estado de habitación es requerido");

        this.estadoHabitacion = estadoHabitacion;
    }

    public void actualizar(Short numero, TipoHabitacion tipoHabitacion, Short capacidad, BigDecimal precio) {
        validarNoEliminado();
        validarDatos(numero, tipoHabitacion, capacidad, precio);
        actualizarTipoHabitacion(tipoHabitacion);

        this.numero = numero;
        this.capacidad = capacidad;
        this.precio = precio;
    }

    public void eliminar() {
        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
}
