package com.kelvin.casepix.handler;

import com.kelvin.casepix.model.dto.alteracao.AlteracaoChaveDTO;
import com.kelvin.casepix.model.dto.inclusao.InclusaoChavePixDTO;
import com.kelvin.casepix.model.entity.ChavePix;
import com.kelvin.casepix.model.entity.TipoChave;
import com.kelvin.casepix.repository.ChavePixRepository;
import com.kelvin.casepix.service.AlteracaoChaveService;
import com.kelvin.casepix.service.impl.AlteracaoChaveServiceImpl;
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
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AlteracaoChavePixHandlerTest {

    @Mock
    private ChavePixRepository chavePixRepository;

    @Test
    public void test() {
        final UUID uuid = UUID.randomUUID();
        ChavePix chavePix = new ChavePix();
        chavePix.setId(uuid);
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
        Mockito.when(this.chavePixRepository.findById(Mockito.any(UUID.class))).thenReturn(Mono.just(chavePix));
        Mockito.when(this.chavePixRepository.existsById(Mockito.any(UUID.class))).thenReturn(Mono.just(Boolean.TRUE));

        final AlteracaoChaveServiceImpl alteracaoChaveService = new AlteracaoChaveServiceImpl(this.chavePixRepository);
        final ValidacaoChaveServiceImpl validacaoChaveService = new ValidacaoChaveServiceImpl();
        final AlteracaoChavePixHandler alteracaoChavePixHandler = new AlteracaoChavePixHandler(alteracaoChaveService, validacaoChaveService);

        final Mono<ServerResponse> serverResponseMono = alteracaoChavePixHandler.alterarChave(new AlteracaoChaveDTO(uuid, "corrente", "001", "0001", "Kelvin", "Garcia"));
        StepVerifier
                .create(serverResponseMono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
