package com.kelvin.casepix.handler;

import com.kelvin.casepix.model.dto.alteracao.AlteracaoChaveDTO;
import com.kelvin.casepix.model.dto.error.ErrorDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import com.kelvin.casepix.service.AlteracaoChaveService;
import com.kelvin.casepix.service.ValidacaoChaveService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AlteracaoChavePixHandler {

    private final AlteracaoChaveService alteracaoChaveService;
    private final ValidacaoChaveService validacaoChaveService;

    public AlteracaoChavePixHandler(AlteracaoChaveService alteracaoChaveService, ValidacaoChaveService validacaoChaveService) {
        this.validacaoChaveService = validacaoChaveService;
        this.alteracaoChaveService = alteracaoChaveService;
    }

    public Mono<ServerResponse> alterarChave(AlteracaoChaveDTO alteracaoChaveDTO) {
        return this.validaTipoConta(alteracaoChaveDTO);
    }

    private Mono<ServerResponse> validaTipoConta(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.validacaoChaveService.validaTipoConta(dto.tipoConta()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.validaNumeroAgencia(alteracaoChaveDTO);
                });
    }

    private Mono<ServerResponse> validaNumeroAgencia(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.validacaoChaveService.validaNumeroAgencia(dto.numeroAgencia()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.validaNumeroConta(alteracaoChaveDTO);
                });
    }

    private Mono<ServerResponse> validaNumeroConta(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.validacaoChaveService.validaNumeroConta(dto.numeroConta()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.validaNomeCorrentista(alteracaoChaveDTO);
                });
    }

    private Mono<ServerResponse> validaNomeCorrentista(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.validacaoChaveService.validaNomeCorrentista(dto.nomeCorrentista()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.validaSobrenomeCorrentista(alteracaoChaveDTO);
                });
    }

    private Mono<ServerResponse> validaSobrenomeCorrentista(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.validacaoChaveService.validaSobrenomeCorrentista(dto.sobrenomeCorrentista()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.verificaId(alteracaoChaveDTO);
                });
    }

    private Mono<ServerResponse> verificaId(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.alteracaoChaveService.existsById(dto.id()))
                .flatMap(exists -> {
                    if(!exists) {
                        return ServerResponse.notFound().build();
                    }
                    return this.alterarComSucesso(alteracaoChaveDTO);
                });
    }

    private Mono<ServerResponse> alterarComSucesso(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.alteracaoChaveService.alterarChave(dto))
                .flatMap(inclusaoResponseDTO -> ServerResponse.ok().body(Mono.just(inclusaoResponseDTO), InclusaoResponseDTO.class));
    }
}
