package com.kelvin.casepix.service.impl;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.kelvin.casepix.model.dto.error.ValidacaoErroDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.service.ValidacaoChaveService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;

@Service
public class ValidacaoChaveServiceImpl implements ValidacaoChaveService {
    @Override
    public Mono<ValidacaoErroDTO> validaChave(InclusaoChavePixDTO inclusaoChavePixDTO) {
        return Mono.just(inclusaoChavePixDTO)
                .map(dto -> {
                    final TipoChave tipoChave = TipoChave.valueOf(dto.tipoChave().toUpperCase(Locale.ROOT));
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
                            if(!dto.valorChave().matches("^[0-9]*$")) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "CPF deve possuir somente números");
                            }
                            final CPFValidator cpfValidator = new CPFValidator();
                            final List<ValidationMessage> validationMessages = cpfValidator.invalidMessagesFor(dto.valorChave());
                            if(!validationMessages.isEmpty()) {
                                return new ValidacaoErroDTO(Boolean.TRUE, this.checkCpfError(validationMessages.get(0).getMessage()));
                            }
                        }
                        case CNPJ -> {
                            if(!dto.valorChave().matches("^[0-9]*$")) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "CNPJ deve possuir somente números");
                            }
                            final CNPJValidator cnpjValidator = new CNPJValidator();
                            final List<ValidationMessage> validationMessages = cnpjValidator.invalidMessagesFor(dto.valorChave());
                            if(!validationMessages.isEmpty()) {
                                return new ValidacaoErroDTO(Boolean.TRUE, this.checkCnpjError(validationMessages.get(0).getMessage()));
                            }
                        }
                        case CHAVE_ALEATORIA -> {
                        }
                    }
                    return new ValidacaoErroDTO(Boolean.FALSE, "");
                });
    }

    private String checkCpfError(String error) {
        return switch (error){
            case "CPFError : INVALID DIGITS" -> "CPF deve conter 11 caracteres";
            case "CPFError : INVALID FORMAT" -> "Formato de CPF inválido";
            case "CPFError : INVALID CHECK DIGITS" -> "CPF inválido";
            case "CPFError : REPEATED DIGITS" -> "Digitos repetidos CPF";
            default -> "";
        };
    }

    private String checkCnpjError(String error) {
        return switch (error){
            case "CNPJError : INVALID DIGITS" -> "CNPJ deve conter 14 caracteres";
            case "CNPJError : INVALID FORMAT" -> "Formato de CNPJ inválido";
            case "CNPJError : INVALID CHECK DIGITS" -> "CNPJ inválido";
            default -> "";
        };
    }
}
