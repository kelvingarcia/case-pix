package com.kelvin.casepix.service.impl;

import com.kelvin.casepix.exception.ChavePixNotFound;
import com.kelvin.casepix.model.dto.consulta.ConsultaResponseDTO;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.ConsultarChaveService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ConsultarChaveServiceImpl implements ConsultarChaveService {

    private final ChavePixRepository chavePixRepository;

    public ConsultarChaveServiceImpl(ChavePixRepository chavePixRepository) {
        this.chavePixRepository = chavePixRepository;
    }

    @Override
    public Mono<ConsultaResponseDTO> consultaPorId(UUID idChave) {
        return Mono.just(idChave)
                .flatMap(id -> this.chavePixRepository.findById(idChave))
                .switchIfEmpty(Mono.error(new ChavePixNotFound("Id não encontrado")))
                .map(chavePix -> new ConsultaResponseDTO(chavePix.getId(), chavePix.getTipoChave(), chavePix.getValorChave(),
                        chavePix.getTipoConta(), chavePix.getNumeroAgencia(), chavePix.getNumeroConta(), chavePix.getNomeCorrentista(),
                        chavePix.getSobrenomeCorrentista(), chavePix.getDataHoraInclusao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        ""));
    }
}
