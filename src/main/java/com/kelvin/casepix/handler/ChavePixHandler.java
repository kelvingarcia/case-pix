package com.kelvin.casepix.handler;

import com.kelvin.casepix.model.dto.error.ErrorDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.service.ChavePixService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
public class ChavePixHandler {

    private final ChavePixService chavePixService;

    public ChavePixHandler(ChavePixService chavePixService) {
        this.chavePixService = chavePixService;
    }

    public Mono<ServerResponse> inclusaoHandler(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return this.chaveDuplicada(inclusaoChavePixDTO);
    }

    private Mono<ServerResponse> quantidadeDeChaves(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.chavePixService.existsByChaveValor(dto.valorChave()))
                .flatMap(exists -> {
                    if(exists) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, "Chave já existente.")), ErrorDTO.class);
                    }
                    return this.salvarComSucesso(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> chaveDuplicada(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.chavePixService.existsByChaveValor(dto.valorChave()))
                .flatMap(exists -> {
                    if(exists) {
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, "Chave já existente.")), ErrorDTO.class);
                    }
                    return this.salvarComSucesso(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> salvarComSucesso(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.chavePixService.inclusao(dto))
                .flatMap(inclusaoResponseDTO -> ServerResponse.ok().body(Mono.just(inclusaoResponseDTO), InclusaoResponseDTO.class));
    }

}
