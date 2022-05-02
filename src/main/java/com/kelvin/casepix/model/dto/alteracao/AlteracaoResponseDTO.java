package com.kelvin.casepix.model.dto.alteracao;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlteracaoResponseDTO(UUID id, String tipoChave, String valorChave, String tipoConta, String numeroAgencia,
                                   String numeroConta, String nomeCorrentista, String sobrenomeCorrentista, LocalDateTime dataHoraInclusaoChave) {
}
