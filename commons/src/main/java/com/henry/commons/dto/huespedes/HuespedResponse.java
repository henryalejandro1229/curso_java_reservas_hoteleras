package com.henry.commons.dto.huespedes;

public record HuespedResponse(

        Long id,
        String nombre,
        String nacionalidad,
        String email,
        String telefono,
        String documento
) {
}
