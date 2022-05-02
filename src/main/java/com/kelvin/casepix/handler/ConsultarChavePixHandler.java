package com.kelvin.casepix.handler;

import com.kelvin.casepix.exception.ChavePixNotFound;
import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import com.kelvin.casepix.service.ConsultarChaveService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class ConsultarChavePixHandler {

    private final ConsultarChaveService consultarChaveService;

    public ConsultarChavePixHandler(ConsultarChaveService consultarChaveService) {
        this.consultarChaveService = consultarChaveService;
    }

    public Mono<ServerResponse> consultar(ServerRequest serverRequest) {
        return Mono.just(serverRequest.queryParams())
                .flatMap(mapValue -> {
                    final String id = mapValue.getFirst("id");
                    final UUID uuid = UUID.fromString(id);
                    return this.consultarChaveService.consultaPorId(uuid);
                })
                .flatMap(consultaResponseDTO -> ServerResponse.ok().body(Mono.just(consultaResponseDTO), ConsultaResponseDTO.class))
                .onErrorResume(e -> ServerResponse.notFound().build());
    }
}
