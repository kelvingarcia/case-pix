package com.kelvin.casepix.handler;

import com.kelvin.casepix.model.dto.error.ErrorDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import com.kelvin.casepix.service.InclusaoChaveService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
public class ChavePixHandler {

    private final InclusaoChaveService inclusaoChaveService;

    public ChavePixHandler(InclusaoChaveService inclusaoChaveService) {
        this.inclusaoChaveService = inclusaoChaveService;
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
                    return this.salvarComSucesso(inclusaoChavePixDTO);
                });
    }

    private Mono<ServerResponse> salvarComSucesso(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .flatMap(dto -> this.inclusaoChaveService.inclusao(dto))
                .flatMap(inclusaoResponseDTO -> ServerResponse.ok().body(Mono.just(inclusaoResponseDTO), InclusaoResponseDTO.class));
    }

}
