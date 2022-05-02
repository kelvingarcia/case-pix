package com.kelvin.casepix.service.impl;

import com.kelvin.casepix.exception.ChavePixNotFoundException;
import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.ConsultarChaveService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.core.query.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ConsultarChaveServiceImpl implements ConsultarChaveService {

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
                .switchIfEmpty(Mono.error(new ChavePixNotFoundException("Id não encontrado")))
                .map(chavePix -> new ConsultaResponseDTO(chavePix.getId(), chavePix.getTipoChave(), chavePix.getValorChave(),
                        chavePix.getTipoConta(), chavePix.getNumeroAgencia(), chavePix.getNumeroConta(), chavePix.getNomeCorrentista(),
                        chavePix.getSobrenomeCorrentista(), chavePix.getDataHoraInclusao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        ""));
    }

    @Override
    public Flux<ConsultaResponseDTO> consultarPorFiltros(ChavePix chavePixAux) {
        return Mono.just(chavePixAux)
                .flatMapMany(chavePix -> {
                    final Query query = new Query();
                    if(chavePix.getTipoChave() != null) {
                        query.addCriteria(Criteria.where("tipoChave").is(chavePix.getTipoChave()));
                    }
                    if(chavePix.getNumeroAgencia() != null && chavePix.getNumeroConta() != null) {
                        query.addCriteria(Criteria.where("numeroAgencia").is(chavePix.getNumeroAgencia()));
                        query.addCriteria(Criteria.where("numeroConta").is(chavePix.getNumeroConta()));
                    }
                    if(chavePix.getNomeCorrentista() != null) {
                        query.addCriteria(Criteria.where("nomeCorrentista").is(chavePix.getNomeCorrentista()));
                    }
                    if(chavePix.getDataHoraInclusao() != null) {
                        query.addCriteria(Criteria.where("dataHoraInclusao").is(chavePix.getDataHoraInclusao()));
                    }
                    if(chavePix.getDataHoraInativacao() != null) {
                        query.addCriteria(Criteria.where("dataHoraInativacao").is(chavePix.getDataHoraInativacao()));
                    }
                    return this.mongoTemplate.find(query, ChavePix.class);
                })
                .map(chavePix -> new ConsultaResponseDTO(chavePix.getId(), chavePix.getTipoChave(), chavePix.getValorChave(),
                        chavePix.getTipoConta(), chavePix.getNumeroAgencia(), chavePix.getNumeroConta(), chavePix.getNomeCorrentista(),
                        chavePix.getSobrenomeCorrentista(), chavePix.getDataHoraInclusao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        ""));
    }
}
