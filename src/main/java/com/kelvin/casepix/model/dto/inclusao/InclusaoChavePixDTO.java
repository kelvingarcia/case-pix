package com.kelvin.casepix.model.dto.inclusao;

public record InclusaoChavePixDTO(String tipoChave, String valorChave, String tipoConta, Integer numeroAgencia,
                                  Integer numeroConta, String nomeCorrentista, String sobrenomeCorrentista, String numeroDocumento) {
}
