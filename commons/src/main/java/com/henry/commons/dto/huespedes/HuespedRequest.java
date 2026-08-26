package com.henry.commons.dto.huespedes;


import jakarta.validation.constraints.*;

public record HuespedRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size (min = 2, max = 50, message = "El apellido Paterno debe tener entre 1  y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size (min = 2, max = 50, message = "El apellido Materno debe tener entre 1  y 50 caracteres")
        String apellidoMaterno,

        @NotBlank(message = "La nacionalidad es requerida")
        @Size (min = 1, max = 50, message = "La nacionalidad debe tener entre 1  y 50 caracteres")
        String nacionalidad,

        @NotBlank(message = "El email es requerido")
        @Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
        @Email(message = "El email debe tener un formato valido (ejemplo@dominio.com)")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos")
        String telefono,

        @NotBlank(message = "El documento es requerido")
        @Size(min = 1, max = 20, message = "El documento debe contar entre  1 y 20 caracteres")
        String documento
) {
}
