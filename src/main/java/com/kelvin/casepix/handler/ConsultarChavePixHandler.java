package com.kelvin.casepix.handler;

import com.kelvin.casepix.exception.ChavePixNotFoundException;
import com.kelvin.casepix.exception.ConsultarValoresException;
import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import com.kelvin.casepix.model.dto.error.ErrorDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.service.ConsultarChaveService;
import com.kelvin.casepix.service.impl.ConsultarChaveServiceImpl;
import com.mongodb.internal.connection.Server;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Component
public class ConsultarChavePixHandler {

    private final ConsultarChaveServiceImpl consultarChaveService;

    public ConsultarChavePixHandler(ConsultarChaveServiceImpl consultarChaveService) {
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

                    if(id != null) {
                        final UUID uuid = UUID.fromString(id);
                        return Mono.just(this.consultarChaveService.consultaPorId(uuid));
                    } else {
                        final Query query = new Query();
                        if(tipoChave != null) {
                            query.addCriteria(Criteria.where("tipoChave").is(TipoChave.valueOf(tipoChave.toUpperCase(Locale.ROOT))));
                        }
                        if(numeroAgencia != null && numeroConta != null) {
                            query.addCriteria(Criteria.where("numeroAgencia").is(numeroAgencia));
                            query.addCriteria(Criteria.where("numeroConta").is(numeroConta));
                        }
                        if(nomeCorrentista != null) {
                            query.addCriteria(Criteria.where("nomeCorrentista").is(nomeCorrentista));
                        }
                        if(dataInclusao != null) {
                            final LocalDateTime dataHoraInclusao = LocalDate.parse(dataInclusao, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            query.addCriteria(Criteria.where("dataHoraInclusao").gte(dataHoraInclusao).lt(dataHoraInclusao.plusDays(1l)));
                        }
                        if(dataInativacao != null) {
                            final LocalDateTime dataHoraInativacao = LocalDate.parse(dataInclusao, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            query.addCriteria(Criteria.where("dataHoraInativacao").gte(dataHoraInativacao).lt(dataHoraInativacao.plusDays(1l)));
                        }
                        return Mono.just(this.consultarChaveService.consultarPorFiltros(query));
                    }
                })
                .flatMap(consultaResponseDTO -> ServerResponse.ok().body(consultaResponseDTO, ConsultaResponseDTO.class));
    }

    public Mono<ServerResponse> validaConsultar(ServerRequest serverRequest) {
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
                        return this.consultarChaveService.containsPorId(uuid);
                    } else {
                        final Query query = new Query();
                        if(tipoChave != null) {
                            query.addCriteria(Criteria.where("tipoChave").is(TipoChave.valueOf(tipoChave.toUpperCase(Locale.ROOT))));
                        }
                        if(numeroAgencia != null && numeroConta != null) {
                            query.addCriteria(Criteria.where("numeroAgencia").is(numeroAgencia));
                            query.addCriteria(Criteria.where("numeroConta").is(numeroConta));
                        }
                        if(nomeCorrentista != null) {
                            query.addCriteria(Criteria.where("nomeCorrentista").is(nomeCorrentista));
                        }
                        if(dataInclusao != null) {
                            final LocalDateTime dataHoraInclusao = LocalDate.parse(dataInclusao, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            query.addCriteria(Criteria.where("dataHoraInclusao").gte(dataHoraInclusao).lt(dataHoraInclusao.plusDays(1l)));
                        }
                        if(dataInativacao != null) {
                            final LocalDateTime dataHoraInativacao = LocalDate.parse(dataInclusao, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            query.addCriteria(Criteria.where("dataHoraInativacao").gte(dataHoraInativacao).lt(dataHoraInativacao.plusDays(1l)));
                        }
                        return this.consultarChaveService.containsPorFiltros(query);
                    }
                })
                .flatMap(contains -> {
                    if(!contains) {
                        return ServerResponse.notFound().build();
                    }
                    return this.consultar(serverRequest);
                })
                .onErrorResume(e -> {
                    if(e instanceof ConsultarValoresException){
                        return ServerResponse.unprocessableEntity().body(Mono.just(new ErrorDTO(422, e.getMessage())), ErrorDTO.class);
                    }
                    return Mono.error(e);
                });
    }
}
