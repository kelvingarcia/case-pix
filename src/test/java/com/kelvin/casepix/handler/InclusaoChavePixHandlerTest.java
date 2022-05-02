package com.kelvin.casepix.handler;

import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.impl.InclusaoChaveServiceImpl;
import com.kelvin.casepix.service.impl.ValidacaoChaveServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class InclusaoChavePixHandlerTest {

    @Mock
    private ChavePixRepository chavePixRepository;

    @Test
    public void test() {
        ChavePix chavePix = new ChavePix();
        chavePix.setId(UUID.randomUUID());
        chavePix.setTipoChave(TipoChave.EMAIL);
        chavePix.setValorChave("kelvin.garcia@gmail.com");
        chavePix.setTipoConta("corrente");
        chavePix.setNumeroConta("0001");
        chavePix.setNumeroAgencia("001");
        chavePix.setNomeCorrentista("Kelvin");
        chavePix.setSobrenomeCorrentista("Garcia");
        chavePix.setDataHoraInclusao(LocalDateTime.now());
        chavePix.setNumeroDocumento("");
        Mockito.when(this.chavePixRepository.save(Mockito.any())).thenReturn(Mono.just(chavePix));
        Mockito.when(this.chavePixRepository.countByNumeroDocumento(Mockito.any())).thenReturn(Mono.just(1l));
        Mockito.when(this.chavePixRepository.existsByValorChave(Mockito.any())).thenReturn(Mono.just(Boolean.FALSE));
        final InclusaoChaveServiceImpl inclusaoChaveService = new InclusaoChaveServiceImpl(this.chavePixRepository);
        final ValidacaoChaveServiceImpl validacaoChaveService = new ValidacaoChaveServiceImpl();
        final InclusaoChavePixHandler inclusaoChavePixHandler = new InclusaoChavePixHandler(inclusaoChaveService, validacaoChaveService);

        final Mono<ServerResponse> serverResponseMono = inclusaoChavePixHandler.inclusaoHandler(new InclusaoChavePixDTO("email", "kelvin.garcia@gmail.com", "corrente", "001", "0001", "Kelvin", "Garcia", ""));
        StepVerifier
                .create(serverResponseMono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
