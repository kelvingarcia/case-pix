package com.kelvin.casepix.service.impl;

import com.kelvin.casepix.model.dto.error.ValidacaoErroDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.service.ValidacaoChaveService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Service
public class ValidacaoChaveServiceImpl implements ValidacaoChaveService {
    @Override
    public Mono<ValidacaoErroDTO> validaChave(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .map(dto -> {
                    TipoChave tipoChave = TipoChave.valueOf(dto.tipoChave().toUpperCase(Locale.ROOT));
                    switch (tipoChave) {
                        case CELULAR -> {
                        }
                        case EMAIL -> {
                            if (!dto.valorChave().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "E-mail fora do padrão");
                            }
                            if (dto.valorChave().length() > 77) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "E-mail com mais de 77 caracteres");
                            }
                        }
                        case CPF -> {
                        }
                        case CNPJ -> {
                        }
                        case CHAVE_ALEATORIA -> {
                        }
                    }
                    return new ValidacaoErroDTO(Boolean.FALSE, "");
                });
    }
}
