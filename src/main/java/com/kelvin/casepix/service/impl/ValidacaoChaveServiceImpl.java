package com.kelvin.casepix.service.impl;

import br.com.caelum.stella.ValidationMessage;
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
                            if (!dto.valorChave().matches("^[0-9]*$")) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "CPF deve possuir somente números");
                            }
                            final CPFValidator cpfValidator = new CPFValidator();
                            final List<ValidationMessage> validationMessages = cpfValidator.invalidMessagesFor(dto.valorChave());
                            if (!validationMessages.isEmpty()) {
                                return new ValidacaoErroDTO(Boolean.TRUE, this.checkCpfError(validationMessages.get(0).getMessage()));
                            }
                        }
                        case CNPJ -> {
                            if (!dto.valorChave().matches("^[0-9]*$")) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "CNPJ deve possuir somente números");
                            }
                            final CNPJValidator cnpjValidator = new CNPJValidator();
                            final List<ValidationMessage> validationMessages = cnpjValidator.invalidMessagesFor(dto.valorChave());
                            if (!validationMessages.isEmpty()) {
                                return new ValidacaoErroDTO(Boolean.TRUE, this.checkCnpjError(validationMessages.get(0).getMessage()));
                            }
                        }
                        case ALEATORIO -> {
                            if (!dto.valorChave().matches("^[a-zA-Z0-9]+$")) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "Chave aleatória deve ser alfanumérica");
                            }
                            if (dto.valorChave().length() > 36) {
                                return new ValidacaoErroDTO(Boolean.TRUE, "Chave aleatória deve ter no máximo 36 caracteres");
                            }
                        }
                    }
                    return new ValidacaoErroDTO(Boolean.FALSE, "");
                });
    }

    @Override
    public Mono<ValidacaoErroDTO> validaTipoConta(String tipoConta) {
        return Mono.just(tipoConta)
                .map(tipo -> {
                    if (!tipo.equals("corrente") && !tipo.equals("poupança")) {
                        return new ValidacaoErroDTO(Boolean.TRUE, "Tipo conta deve ser corrente ou poupança");
                    }
                    if (tipo.length() > 10) {
                        return new ValidacaoErroDTO(Boolean.TRUE, "Tipo conta deve ter no máximo 10 caracteres");
                    }
                    return new ValidacaoErroDTO(Boolean.FALSE, "");
                });
    }

    @Override
    public Mono<ValidacaoErroDTO> validaNumeroAgencia(String numeroAgencia) {
        return Mono.just(numeroAgencia)
                .map(numero -> {
                    if (!numero.matches("^[0-9]*$")) {
                        return new ValidacaoErroDTO(Boolean.TRUE, "Número agência deve possuir somente números");
                    }
                    if (numero.length() > 4) {
                        return new ValidacaoErroDTO(Boolean.TRUE, "Número agência deve possuir no máximo 4 caracteres");
                    }
                    return new ValidacaoErroDTO(Boolean.FALSE, "");
                });
    }

    @Override
    public Mono<ValidacaoErroDTO> validaNumeroConta(String numeroConta) {
        return Mono.just(numeroConta)
                .map(numero -> {
                    if (!numero.matches("^[0-9]*$")) {
                        return new ValidacaoErroDTO(Boolean.TRUE, "Número conta deve possuir somente números");
                    }
                    if (numero.length() > 8) {
                        return new ValidacaoErroDTO(Boolean.TRUE, "Número conta deve possuir no máximo 8 caracteres");
                    }
                    return new ValidacaoErroDTO(Boolean.FALSE, "");
                });
    }

    private String checkCpfError(String error) {
        return switch (error) {
            case "CPFError : INVALID DIGITS" -> "CPF deve conter 11 caracteres";
            case "CPFError : INVALID FORMAT" -> "Formato de CPF inválido";
            case "CPFError : INVALID CHECK DIGITS" -> "CPF inválido";
            case "CPFError : REPEATED DIGITS" -> "Digitos repetidos CPF";
            default -> "";
        };
    }

    private String checkCnpjError(String error) {
        return switch (error) {
            case "CNPJError : INVALID DIGITS" -> "CNPJ deve conter 14 caracteres";
            case "CNPJError : INVALID FORMAT" -> "Formato de CNPJ inválido";
            case "CNPJError : INVALID CHECK DIGITS" -> "CNPJ inválido";
            default -> "";
        };
    }
}
