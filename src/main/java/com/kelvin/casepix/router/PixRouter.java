package com.kelvin.casepix.router;

import com.kelvin.casepix.handler.AlteracaoChavePixHandler;
import com.kelvin.casepix.handler.InclusaoChavePixHandler;
import com.kelvin.casepix.model.dto.alteracao.AlteracaoChaveDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PixRouter {
    @Bean
    public RouterFunction<ServerResponse> routes(InclusaoChavePixHandler handler, AlteracaoChavePixHandler alteracaoHandler) {
        return route()
            .GET("/list-chave", req -> ok().body(Mono.just(new ChavePix()), ChavePix.class))
            .POST("/incluir", req -> req.bodyToMono(InclusaoChavePixDTO.class).flatMap(dto -> handler.inclusaoHandler(dto)))
            .PUT("/alterar", req -> req.bodyToMono(AlteracaoChaveDTO.class).flatMap(dto -> alteracaoHandler.alterarChave(dto)))
            .build();
    }
}
