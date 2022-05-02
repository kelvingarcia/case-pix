package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ConsultarChaveService {
    Mono<ConsultaResponseDTO> consultaPorId(UUID id);
    Flux<ConsultaResponseDTO> consultarPorFiltros(ChavePix chavePixAux);
}
