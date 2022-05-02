package com.kelvin.casepix.service.impl;

import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.ConsultarChaveService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.core.query.Query;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ConsultarChaveServiceImpl implements ConsultarChaveService{

    private final ChavePixRepository chavePixRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public ConsultarChaveServiceImpl(ChavePixRepository chavePixRepository, ReactiveMongoTemplate mongoTemplate) {
        this.chavePixRepository = chavePixRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<ConsultaResponseDTO> consultaPorId(UUID idChave) {
        return Mono.just(idChave)
                .flatMap(id -> this.chavePixRepository.findById(idChave))
                .map(chavePix -> new ConsultaResponseDTO(chavePix.getId(), chavePix.getTipoChave(), chavePix.getValorChave(),
                        chavePix.getTipoConta(), chavePix.getNumeroAgencia(), chavePix.getNumeroConta(), chavePix.getNomeCorrentista(),
                        chavePix.getSobrenomeCorrentista(), chavePix.getDataHoraInclusao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        ""));
    }

    @Override
    public Flux<ConsultaResponseDTO> consultarPorFiltros(Query query) {
        return this.mongoTemplate.find(query, ChavePix.class)
                .map(chavePix -> new ConsultaResponseDTO(chavePix.getId(), chavePix.getTipoChave(), chavePix.getValorChave(),
                        chavePix.getTipoConta(), chavePix.getNumeroAgencia(), chavePix.getNumeroConta(), chavePix.getNomeCorrentista(),
                        chavePix.getSobrenomeCorrentista(), chavePix.getDataHoraInclusao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        ""));
    }

    @Override
    public Mono<Boolean> containsPorId(UUID idChave) {
        return this.chavePixRepository.existsById(idChave);
    }
    @Override
    public Mono<Boolean> containsPorFiltros(Query query) {
        return this.mongoTemplate.exists(query, ChavePix.class);
    }
}
