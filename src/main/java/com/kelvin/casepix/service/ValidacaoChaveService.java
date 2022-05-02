package com.kelvin.casepix.service;

import com.kelvin.casepix.model.dto.error.ValidacaoErroDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import reactor.core.publisher.Mono;

public interface ValidacaoChaveService {
    Mono<ValidacaoErroDTO> validaChave(InclusaoChavePixDTO inclusaoChavePixDTO);
    Mono<ValidacaoErroDTO> validaTipoConta(String tipoConta);
    Mono<ValidacaoErroDTO> validaNumeroAgencia(String numeroAgencia);
    Mono<ValidacaoErroDTO> validaNumeroConta(String numeroConta);
    Mono<ValidacaoErroDTO> validaNomeCorrentista(String nomeCorrentista);
    Mono<ValidacaoErroDTO> validaSobrenomeCorrentista(String nomeCorrentista);
}
