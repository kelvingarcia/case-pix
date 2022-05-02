package com.kelvin.casepix.handler;

import com.kelvin.casepix.exception.ConsultarValoresException;
import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import com.kelvin.casepix.model.dto.error.ErrorDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.service.ConsultarChaveService;
import com.mongodb.internal.connection.Server;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;
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
                    final String tipoChave = mapValue.getFirst("tipoChave");
                    final String numeroAgencia = mapValue.getFirst("numeroAgencia");
                    final String numeroConta = mapValue.getFirst("numeroConta");
                    final String nomeCorrentista = mapValue.getFirst("nomeCorrentista");
                    final String dataInclusao = mapValue.getFirst("dataInclusao");
                    final String dataInativacao = mapValue.getFirst("dataInativacao");

                    if(id != null && (tipoChave != null || numeroAgencia != null || numeroConta != null || nomeCorrentista != null || dataInclusao != null || dataInativacao != null)){
                        return Mono.error(new ConsultarValoresException("Consulta por id não aceita outros filtros"));
                    }
                    if(dataInclusao != null && dataInativacao != null) {
                        return Mono.error(new ConsultarValoresException("Não é permitida a combinação dos filtros Data de inclusão e Data da inativação"));
                    }
                    if((numeroAgencia != null && numeroConta == null) || (numeroConta != null && numeroAgencia == null)) {
                        return Mono.error(new ConsultarValoresException("É necessário informar numero agencia e numero conta"));
                    }
                    if(id != null) {
                        final UUID uuid = UUID.fromString(id);
                        return Mono.just(this.consultarChaveService.consultaPorId(uuid));
                    } else {
                        final ChavePix chavePixAux = new ChavePix();
                        if(tipoChave != null) {
                            chavePixAux.setTipoChave(TipoChave.valueOf(tipoChave.toUpperCase(Locale.ROOT)));
                        }
                        chavePixAux.setNumeroAgencia(numeroAgencia);
                        chavePixAux.setNumeroConta(numeroConta);
                        chavePixAux.setNomeCorrentista(nomeCorrentista);
                        return Mono.just(this.consultarChaveService.consultarPorFiltros(chavePixAux));
                    }
                })
                .flatMap(consultaResponseDTO -> ServerResponse.ok().body(consultaResponseDTO, ConsultaResponseDTO.class))
                .onErrorResume(e -> {
                    if(e instanceof ConsultarValoresException){
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, e.getMessage())), ErrorDTO.class);
                    }
                    return ServerResponse.notFound().build();
                });
    }
}
