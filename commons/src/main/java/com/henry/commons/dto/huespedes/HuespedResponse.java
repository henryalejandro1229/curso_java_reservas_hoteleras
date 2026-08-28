package com.henry.commons.dto.huespedes;

public record HuespedResponse(

        Long id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String nacionalidad,
        String email,
        String telefono,
        String documento
) {
}
