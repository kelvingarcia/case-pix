package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.alteracao.AlteracaoChaveDTO;
import com.kelvin.casepix.model.dto.alteracao.AlteracaoResponseDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AlteracaoChaveService {
    Mono<AlteracaoResponseDTO> alterarChave(AlteracaoChaveDTO alteracaoChaveDTO);
    Mono<Boolean> existsById(UUID id);
}
