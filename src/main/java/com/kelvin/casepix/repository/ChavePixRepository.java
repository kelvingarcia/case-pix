package com.kelvin.casepix.repository;

import com.kelvin.casepix.model.entity.ChavePix;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ChavePixRepository extends ReactiveCrudRepository<ChavePix, String> {
    Mono<Boolean> existsByValorChave(String valorChave);
    Mono<Long> countByNumeroDocumento(String numeroDocumento);
}
