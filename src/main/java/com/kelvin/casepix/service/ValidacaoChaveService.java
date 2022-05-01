package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.error.ValidacaoErroDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import reactor.core.publisher.Mono;

public interface ValidacaoChaveService {
    Mono<ValidacaoErroDTO> validaChave(InclusaoChavePixDTO inclusaoChavePixDTO);
}
