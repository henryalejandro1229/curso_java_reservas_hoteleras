package com.henry.commons.enums;

import com.henry.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum EstadoReserva {
    CONFIRMADA(1L, "Reserva creada",false){
        @Override
        public Set<EstadoReserva> puedeCambiar(){
            return Set.of(EN_CURSO,CANCELADA);
        }
    },
    EN_CURSO(2L, "Check-in realizado",false){
        @Override
        public Set<EstadoReserva> puedeCambiar(){
            return Set.of(FINALIZADA);
        }
    },
    FINALIZADA(3L, "Check-out realizado",true){
        @Override
        public Set<EstadoReserva> puedeCambiar(){
            return Set.of();
        }
    },
    CANCELADA(4L, "Reserva cancelada", true){
        @Override
        public Set<EstadoReserva> puedeCambiar(){
            return Set.of();
        }
    };

    private final Long codigo;
    private final String descripcion;
    private final boolean eliminable;

    public abstract Set<EstadoReserva> puedeCambiar();
    public boolean puedeCambiarA(EstadoReserva nuevoEstado){
        return puedeCambiar().contains(nuevoEstado);
    }

    public static EstadoReserva ObtenerEstadoReservaPorCodigo(Long codigo) {
        for (EstadoReserva e : values()) {
            if(Objects.equals(e.codigo, codigo))
                return e;
        }

        throw new RecursoNoEncontradoException("Estado reserva no válido: " + codigo);
    }
}