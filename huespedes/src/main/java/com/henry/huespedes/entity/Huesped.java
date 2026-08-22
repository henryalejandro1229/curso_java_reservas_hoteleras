package com.henry.huespedes.entity;

import com.henry.commons.enums.EstadoHabitacion;
import com.henry.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "HUESPEDES")
@AllArgsConstructor
@NoArgsConstructor

public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APEELLIDO_PATERNO", length = 50, nullable = false)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apellidoMaterno;

    @Column(name = "NACIONALIDAD", length = 50, nullable = false)
    private String nacionalidad;

    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "DOCUMENTO")
    private String documento;

    @Column(name = "PRECIO")
    private Double precio;

    @Column(name = "ESTADO_HABITACION")
    private EstadoHabitacion estadoHabitacion;

    @Column(name = "ESTADO_REGISTRO")
    private EstadoRegistro estadoRegistro;
}
