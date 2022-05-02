package com.kelvin.casepix.router;

import com.kelvin.casepix.exception.ChavePixNotFoundException;
import com.kelvin.casepix.exception.ConsultarValoresException;
import com.kelvin.casepix.handler.AlteracaoChavePixHandler;
import com.kelvin.casepix.handler.ConsultarChavePixHandler;
import com.kelvin.casepix.handler.InclusaoChavePixHandler;
import com.kelvin.casepix.model.dto.alteracao.AlteracaoChaveDTO;
import com.kelvin.casepix.model.dto.error.ErrorDTO;
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
    public RouterFunction<ServerResponse> routes(InclusaoChavePixHandler handler, AlteracaoChavePixHandler alteracaoHandler, ConsultarChavePixHandler consultarHandler) {
        return route()
            .GET("/consultar", consultarHandler::consultar)
            .POST("/incluir", req -> req.bodyToMono(InclusaoChavePixDTO.class).flatMap(dto -> handler.inclusaoHandler(dto)))
            .PUT("/alterar", req -> req.bodyToMono(AlteracaoChaveDTO.class).flatMap(dto -> alteracaoHandler.alterarChave(dto)))
            .build();
    }
}
