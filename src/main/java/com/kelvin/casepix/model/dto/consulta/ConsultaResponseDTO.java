package com.kelvin.casepix.model.dto.consulta;

import com.kelvin.casepix.model.entity.TipoChave;

import java.util.UUID;

public record ConsultaResponseDTO(UUID id, TipoChave tipoChave, String valorChave, String tipoConta, String numeroAgencia,
                                  String numeroConta, String nomeCorrentista, String sobrenomeCorrentista, String dataHoraInclusao, String dataHoraInativacao) {
}
