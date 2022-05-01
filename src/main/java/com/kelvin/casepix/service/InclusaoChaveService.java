package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import reactor.core.publisher.Mono;

public interface InclusaoChaveService {
    Mono<InclusaoResponseDTO> inclusao(InclusaoChavePixDTO inclusaoChavePixDTO);
    Mono<Boolean> existsByChaveValor(String chaveValor);
    Mono<Boolean> excedeuQuantidadeDeChaves(String numeroDocumento);
}
