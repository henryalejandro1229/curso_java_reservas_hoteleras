package com.henry.habitaciones.service;

import com.henry.commons.dto.habitaciones.HabitacionRequest;
import com.henry.commons.dto.habitaciones.HabitacionResponse;
import com.henry.commons.enums.EstadoHabitacion;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.enums.TipoHabitacion;
import com.henry.commons.exceptions.RecursoNoEncontradoException;
import com.henry.habitaciones.entity.Habitacion;
import com.henry.habitaciones.mapper.HabitacionMapper;
import com.henry.habitaciones.repository.HabitacionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;

    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        log.info("Listando todas las habitaciones activas");

        return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(habitacionMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        log.info("Buscando habitación activa con id: {}", id);

        return habitacionMapper.entidadAResponse(obtenerHabitacionActivaOException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id) {
        log.info("Buscando habitación sin estado con id: {}", id);

        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación sin estado no encontrada con id: " + id)));
    }

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {
        log.info("Registrando nueva habitación {}", request.numero());

        validarDatosUnicos(request);

        TipoHabitacion tipoHabitacion = TipoHabitacion.ObtenerTipoHabitacionPorCodigo(request.idTipoHabitacion());

        Habitacion habitacion = habitacionMapper.requestAEntidad(request);

        habitacion.actualizarTipoHabitacion(tipoHabitacion);

        habitacionRepository.save(habitacion);

        log.info("Nueva habitación registrada: {}", habitacion.getNumero());

        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);

        log.info("Actualizando habitación con id: {}", id);

        validarCambiosUnicos(request, id);

        habitacion.actualizar(
                request.numero(),
                TipoHabitacion.ObtenerTipoHabitacionPorCodigo(request.idTipoHabitacion()),
                request.capacidad(),
                request.precio()
        );

        log.info("Habitación actualizada exitosamente");

        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public void actualizarEstadoHabitacion(Long idHabitacion, Long idEstado) {
        log.info("Actualizando estado de habitación con idEstado: {}", idEstado);

        Habitacion habitacion = obtenerHabitacionActivaOException(idHabitacion);

        EstadoHabitacion nuevoEstado = EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstado);

        habitacion.actualizarEstadoHabitacion(nuevoEstado);

        log.info("Estado de habitación con id: {} actualizado a: {}",
                idHabitacion, nuevoEstado);
    }

    @Override
    public void eliminar(Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);

        log.info("Eliminando habitación con id: {}", id);

        habitacion.eliminar();

        log.info("Habitación eliminada exitosamente");
    }

    private Habitacion obtenerHabitacionActivaOException(Long id) {
        return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación activa no encontrada con id: " + id
                ));
    }

    private void validarDatosUnicos(HabitacionRequest request) {
        log.info("Validando número de habitación único...");

        if (habitacionRepository.existsByNumeroAndEstadoRegistro(request.numero(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException(
                    "Ya existe una habitación activa registrada con el número: " + request.numero());
    }

    private void validarCambiosUnicos(HabitacionRequest request, Long id) {
        log.info("Validando número de habitación único...");

        if (habitacionRepository.existsByNumeroAndEstadoRegistroAndIdNot(
                request.numero(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException(
                    "Ya existe una habitación activa registrada con el número: " + request.numero());
    }
}
