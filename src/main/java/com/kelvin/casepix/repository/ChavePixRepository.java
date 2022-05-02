package com.kelvin.casepix.repository;

import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ChavePixRepository extends ReactiveCrudRepository<ChavePix, UUID> {
    Mono<Boolean> existsByValorChave(String valorChave);
    Mono<Long> countByNumeroDocumento(String numeroDocumento);
    @Query("{ 'tipoChave' : ?0, 'numeroAgencia': ?1, 'numeroConta': ?2, 'nomeCorrentista': ?3, 'dataHoraInclusao': ?4, 'dataHoraInativacao': ?5 }")
    Mono<ChavePix> findByFiltros(TipoChave tipoChave, String numeroAgencia, String numeroConta, String nomeCorrentista, LocalDateTime dataInclusao, LocalDateTime dataInativacao);
}
