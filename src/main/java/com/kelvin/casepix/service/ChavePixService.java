package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import reactor.core.publisher.Mono;

public interface ChavePixService {
    Mono<InclusaoResponseDTO> inclusao(InclusaoChavePixDTO inclusaoChavePixDTO);
    Mono<Boolean> existsByChaveValor(String chaveValor);
}
