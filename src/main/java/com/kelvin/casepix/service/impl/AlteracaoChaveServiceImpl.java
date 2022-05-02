package com.kelvin.casepix.service.impl;

import com.kelvin.casepix.model.dto.alteracao.AlteracaoChaveDTO;
import com.kelvin.casepix.model.dto.alteracao.AlteracaoResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.AlteracaoChaveService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AlteracaoChaveServiceImpl implements AlteracaoChaveService {

    private final ChavePixRepository chavePixRepository;

    public AlteracaoChaveServiceImpl(ChavePixRepository chavePixRepository) {
        this.chavePixRepository = chavePixRepository;
    }

    @Override
    public Mono<AlteracaoResponseDTO> alterarChave(AlteracaoChaveDTO alteracaoChaveDTO) {
        return Mono.just(alteracaoChaveDTO)
                .flatMap(dto -> this.chavePixRepository.findById(dto.id()))
                .flatMap(chave -> {
                    chave.setTipoConta(alteracaoChaveDTO.tipoConta());
                    chave.setNumeroAgencia(alteracaoChaveDTO.numeroAgencia());
                    chave.setNumeroConta(alteracaoChaveDTO.numeroConta());
                    chave.setNomeCorrentista(alteracaoChaveDTO.nomeCorrentista());
                    chave.setSobrenomeCorrentista(alteracaoChaveDTO.sobrenomeCorrentista());

                    return this.chavePixRepository.save(chave);
                })
                .map(chave -> new AlteracaoResponseDTO(chave.getId(), chave.getTipoChave(), chave.getValorChave(),
                        chave.getTipoConta(), chave.getNumeroAgencia(), chave.getNumeroConta(), chave.getNomeCorrentista(),
                        chave.getSobrenomeCorrentista(), chave.getDataHoraInclusao()));
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.chavePixRepository.existsById(id);
    }
}
