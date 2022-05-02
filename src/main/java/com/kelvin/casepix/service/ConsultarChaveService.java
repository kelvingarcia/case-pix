package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ConsultarChaveService {
    Mono<ConsultaResponseDTO> consultaPorId(UUID idChave);
    Flux<ConsultaResponseDTO> consultarPorFiltros(Query query);
    Mono<Boolean> containsPorId(UUID idChave);
    Mono<Boolean> containsPorFiltros(Query query);
}
