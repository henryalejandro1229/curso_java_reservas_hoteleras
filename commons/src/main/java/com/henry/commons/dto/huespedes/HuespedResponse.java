package com.henry.commons.dto.huespedes;

import com.henry.commons.enums.EstadoRegistro;

public record HuespedResponse(

        Long id,
        String nombre,
        String nacionalidad,
        String email,
        String telefono,
        String documento
) {
}
