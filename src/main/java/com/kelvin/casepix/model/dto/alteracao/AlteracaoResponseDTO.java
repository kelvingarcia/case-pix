package com.kelvin.casepix.model.dto.alteracao;

import com.kelvin.casepix.model.entity.TipoChave;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlteracaoResponseDTO(UUID id, TipoChave tipoChave, String valorChave, String tipoConta, String numeroAgencia,
                                   String numeroConta, String nomeCorrentista, String sobrenomeCorrentista, LocalDateTime dataHoraInclusaoChave) {
}
