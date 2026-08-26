package com.henry.huespedes.entity;

import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "HUESPEDES")
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HUESPED")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO")
    private EstadoRegistro estadoRegistro;

    public void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno, String nacionalidad, String email, String telefono, String documento){
        StringCustomUtils.validarTamanio(nombre, 2, 50,
                "El nombre es requerido y debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 2, 50,
                "El apellido paterno es requerido y debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 2, 50,
                "El apellido materno es requerido y debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(nacionalidad, 1, 50,
                "La nacionalidad es requerida y debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(email, 1, 100,
                "El email es requerido y debe contener entre 1 y 100 caracteres");
        StringCustomUtils.validarTamanio(telefono, 10, 10,
                "El teléfono es requerido y debe contener 10 caracteres");
        StringCustomUtils.validarTamanio(documento, 1, 20,
                "El documento es requerido y debe contener 10 caracteres");
    }

    private void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalArgumentException("El huesped ya está eliminado");
    }

    public void eliminar() {
        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void  actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, String nacionalidad, String email, String telefono, String documento) {
        validarNoEliminado();
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, nacionalidad, email, telefono,documento);
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nacionalidad = nacionalidad;
        this.email = email;
        this.telefono = telefono;
        this.documento = documento;
    }
}
