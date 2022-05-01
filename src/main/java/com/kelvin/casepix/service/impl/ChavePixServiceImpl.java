package com.kelvin.casepix.service.impl;

import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.ChavePixService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ChavePixServiceImpl implements ChavePixService {

    private final ChavePixRepository chavePixRepository;

    public ChavePixServiceImpl(ChavePixRepository chavePixRepository) {
        this.chavePixRepository = chavePixRepository;
    }

    @Override
    public Mono<InclusaoResponseDTO> inclusao(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .map(dto -> {
                    ChavePix chavePix = new ChavePix();
                    chavePix.setTipoChave(dto.tipoChave());
                    chavePix.setValorChave(dto.valorChave());
                    chavePix.setTipoConta(dto.tipoConta());
                    chavePix.setNumeroConta(dto.numeroConta());
                    chavePix.setNumeroAgencia(dto.numeroAgencia());
                    chavePix.setNomeCorrentista(dto.nomeCorrentista());
                    chavePix.setSobrenomeCorrentista(dto.sobrenomeCorrentista());
                    chavePix.setDataHoraInclusao(LocalDateTime.now());
                    chavePix.setNumeroDocumento(inclusaoChavePixDTO.numeroDocumento());
                    return chavePix;
                })
                .flatMap(chavePix -> this.chavePixRepository.save(chavePix))
                .map(chavePix -> new InclusaoResponseDTO(chavePix.getId()));
    }
    @Override
    public Mono<Boolean> existsByChaveValor(String chaveValor) {
        return this.chavePixRepository.existsByValorChave(chaveValor);
    }
}
