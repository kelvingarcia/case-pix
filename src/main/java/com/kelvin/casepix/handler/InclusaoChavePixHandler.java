package com.kelvin.casepix.handler;

import com.kelvin.casepix.model.dto.error.ErrorDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import com.kelvin.casepix.service.InclusaoChaveService;
import com.kelvin.casepix.service.ValidacaoChaveService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
public class InclusaoChavePixHandler {

    private final InclusaoChaveService inclusaoChaveService;
    private final ValidacaoChaveService validacaoChaveService;

    public InclusaoChavePixHandler(InclusaoChaveService inclusaoChaveService, ValidacaoChaveService validacaoChaveService) {
        this.inclusaoChaveService = inclusaoChaveService;
        this.validacaoChaveService = validacaoChaveService;
    }

    public Mono<ServerResponse> inclusaoHandler(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return this.quantidadeDeChaves(inclusaoChavePixDTO);
    }

    private Mono<ServerResponse> quantidadeDeChaves(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.inclusaoChaveService.excedeuQuantidadeDeChaves(dto.numeroDocumento()))
                .flatMap(excedeu -> {
                    if(excedeu) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, "Cliente já possui 5 chaves.")), ErrorDTO.class);
                    }
                    return this.chaveDuplicada(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> chaveDuplicada(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.inclusaoChaveService.existsByChaveValor(dto.valorChave()))
                .flatMap(exists -> {
                    if(exists) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, "Chave já existente.")), ErrorDTO.class);
                    }
                    return this.validaTipoChave(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> validaTipoChave(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.validacaoChaveService.validaChave(dto))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.validaTipoConta(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> validaTipoConta(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.validacaoChaveService.validaTipoConta(dto.tipoConta()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.validaNumeroAgencia(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> validaNumeroAgencia(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.validacaoChaveService.validaNumeroAgencia(dto.numeroAgencia()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.validaNumeroConta(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> validaNumeroConta(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.validacaoChaveService.validaNumeroConta(dto.numeroConta()))
                .flatMap(validacaoErroDTO -> {
                    if(validacaoErroDTO.isError()) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, validacaoErroDTO.errorMessage())), ErrorDTO.class);
                    }
                    return this.salvarComSucesso(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> salvarComSucesso(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.inclusaoChaveService.inclusao(dto))
                .flatMap(inclusaoResponseDTO -> ServerResponse.ok().body(Mono.just(inclusaoResponseDTO), InclusaoResponseDTO.class));
    }

}
