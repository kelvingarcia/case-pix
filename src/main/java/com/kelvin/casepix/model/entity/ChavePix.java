package com.kelvin.casepix.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class ChavePix {
    private String id;
    private String tipoChave;
    private String valorChave;
    private String tipoConta;
    private Integer numeroAgencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;
    private String numeroDocumento;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraInativacao;

    public ChavePix() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValorChave() {
        return valorChave;
    }

    public void setValorChave(String valorChave) {
        this.valorChave = valorChave;
    }

    public String getTipoChave() {
        return tipoChave;
    }

    public void setTipoChave(String tipoChave) {
        this.tipoChave = tipoChave;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Integer getNumeroAgencia() {
        return numeroAgencia;
    }

    public void setNumeroAgencia(Integer numeroAgencia) {
        this.numeroAgencia = numeroAgencia;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getNomeCorrentista() {
        return nomeCorrentista;
    }

    public void setNomeCorrentista(String nomeCorrentista) {
        this.nomeCorrentista = nomeCorrentista;
    }

    public String getSobrenomeCorrentista() {
        return sobrenomeCorrentista;
    }

    public void setSobrenomeCorrentista(String sobrenomeCorrentista) {
        this.sobrenomeCorrentista = sobrenomeCorrentista;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public LocalDateTime getDataHoraInativacao() {
        return dataHoraInativacao;
    }

    public void setDataHoraInativacao(LocalDateTime dataHoraInativacao) {
        this.dataHoraInativacao = dataHoraInativacao;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}
