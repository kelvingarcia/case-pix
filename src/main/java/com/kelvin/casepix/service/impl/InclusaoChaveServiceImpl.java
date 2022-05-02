package com.kelvin.casepix.service.impl;

import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoResponseDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.InclusaoChaveService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class InclusaoChaveServiceImpl implements InclusaoChaveService {

    private final ChavePixRepository chavePixRepository;

    public InclusaoChaveServiceImpl(ChavePixRepository chavePixRepository) {
        this.chavePixRepository = chavePixRepository;
    }

    @Override
    public Mono<InclusaoResponseDTO> inclusao(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .map(dto -> {
                    ChavePix chavePix = new ChavePix();
                    chavePix.setId(UUID.randomUUID());
                    chavePix.setTipoChave(TipoChave.valueOf(dto.tipoChave().toUpperCase(Locale.ROOT)));
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
                .map(chavePix -> new InclusaoResponseDTO(chavePix.getId().toString()));
    }
    @Override
    public Mono<Boolean> existsByChaveValor(String chaveValor) {
        return this.chavePixRepository.existsByValorChave(chaveValor);
    }

    @Override
    public Mono<Boolean> excedeuQuantidadeDeChaves(String numeroDocumento) {
        return this.chavePixRepository.countByNumeroDocumento(numeroDocumento)
                .map(quantidade -> quantidade >= 5l);
    }
}
