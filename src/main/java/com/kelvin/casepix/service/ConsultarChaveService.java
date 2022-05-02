package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ConsultarChaveService {
    Mono<ConsultaResponseDTO> consultaPorId(UUID id);
}
