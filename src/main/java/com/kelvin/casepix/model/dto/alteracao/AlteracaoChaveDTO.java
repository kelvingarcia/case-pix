package com.kelvin.casepix.model.dto.alteracao;

import java.util.UUID;

public record AlteracaoChaveDTO(UUID id, String tipoConta, String numeroAgencia,
                                String numeroConta, String nomeCorrentista, String sobrenomeCorrentista) {
}
