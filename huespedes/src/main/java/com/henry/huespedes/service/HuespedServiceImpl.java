package com.henry.huespedes.service;


import com.henry.commons.dto.huespedes.HuespedRequest;
import com.henry.commons.dto.huespedes.HuespedResponse;
import com.henry.commons.enums.EstadoRegistro;
import com.henry.commons.exceptions.EntidadRelacionadaException;
import com.henry.commons.exceptions.RecursoNoEncontradoException;
import com.henry.huespedes.entity.Huesped;
import com.henry.huespedes.mapper.HuespedMapper;
import com.henry.huespedes.repository.HuespedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HuespedServiceImpl implements HuespedService {

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;

    @Override
    public HuespedResponse obtenerHuespedPorIdSinEstado(Long id) {
        Huesped huesped = huespedRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontradoException("Huesped no encontrado con id: " + id));
        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public List<HuespedResponse> listar() {
        log.info("Listando todos los huespedes activos");
        return huespedRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(huespedMapper::entidadAResponse).toList();
    }

    @Override
    public HuespedResponse obtenerPorId(Long id) {
        return huespedMapper.entidadAResponse(obtenerHuespedActivoOException(id));
    }

    @Override
    public HuespedResponse registrar(HuespedRequest request) {
        log.info("Registrando nuevo huesped {}", request.nombre());
        validarDatosUnicos(request);
        Huesped huesped = huespedMapper.requestAEntidad(request);
        huespedRepository.save(huesped);
        log.info("Nuevo huesped registrado {}", request.nombre());
        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public HuespedResponse actualizar(HuespedRequest request, long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);
        log.info("Actualizando huesped activo {}", huesped.getId());
        validarCambiosUnicos(request, id);
        huesped.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.nacionalidad(),
                request.email(),
                request.telefono(),
                request.documento()
        );
        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public void eliminar(Long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);
        log.info("eliminando huesped activo {}", id);
        huesped.eliminar();
        huespedRepository.save(huesped);
        log.info("Huesped con id {} marcado com eliminado", id);
    }

    private Huesped obtenerHuespedActivoOException(Long id) {
        log.info("Buscando huesped activo con id {}", id);
        return huespedRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Huesped activo no encontrado con id: " + id));
    }

    private void validarDatosUnicos(HuespedRequest request){
        log.info("Validando email unico...");
        if (huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistro(
                request.email().trim(), EstadoRegistro.ACTIVO))
            throw new EntidadRelacionadaException("Ya existe un huesped activo registrado con el email: "  + request.email());

        log.info("Validando telefono...");
        if (huespedRepository.existsByTelefonoAndEstadoRegistro(
                request.telefono().trim(), EstadoRegistro.ACTIVO))
            throw new EntidadRelacionadaException("Ya existe un huesped activo registrado con el telefono: "  + request.telefono());

        log.info("Validando documento unico...");
        if (huespedRepository.existsByDocumentoAndEstadoRegistro(
                request.documento().trim(), EstadoRegistro.ACTIVO))
            throw new EntidadRelacionadaException("Ya existe un huesped activo registrado con el documento: " + request.documento());
    }

    private void validarCambiosUnicos(HuespedRequest request, Long id) {
        log.info("Validando cambio en email unico...");
        if (huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                request.email().trim(), EstadoRegistro.ACTIVO, id))
            throw new EntidadRelacionadaException("Ya existe un huesped activo registrado con el email: "  + request.email());

        log.info("Validando telefono...");
        if (huespedRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
                request.telefono().trim(), EstadoRegistro.ACTIVO, id))
            throw new EntidadRelacionadaException("Ya existe un huesped activo registrado con el telefono: "  + request.telefono());

        log.info("Validando documento...");
        if (huespedRepository.existsByDocumentoAndEstadoRegistroAndIdNot(
                request.documento().trim(), EstadoRegistro.ACTIVO, id))
            throw new EntidadRelacionadaException("Ya existe un huesped activo registrado con el documento: "  + request.documento());
    }
}
