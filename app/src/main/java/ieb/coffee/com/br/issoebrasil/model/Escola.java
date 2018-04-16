package ieb.coffee.com.br.issoebrasil.model;

import java.util.List;

public class Escola {
    private String codEscola;
    private String nome;
    private String rede;
    private String email;
    private String esferaAdministrativa;
    private String categoriaEscolaPrivada;
    private String situacaoFuncionamento;
    private String seFimLucrativo;
    private String seConveniadaSetorPublico;
    private Double qtdSalasExistentes;
    private Double qtdSalasUtilizadas;
    private Double qtdFuncionarios;
    private Double qtdComputadores;
    private Double qtdComputadoresPorAluno;
    private Double qtdAlunos;
    private String zona;
    private Endereco endereco;
    private Infraestrutura infraestrutura;

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Infraestrutura getInfraestrutura() {
        return infraestrutura;
    }

    public void setInfraestrutura(Infraestrutura infraestrutura) {
        this.infraestrutura = infraestrutura;
    }

    public String getCodEscola() {
        return codEscola;
    }

    public void setCodEscola(String codEscola) {
        this.codEscola = codEscola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRede() {
        return rede;
    }

    public void setRede(String rede) {
        this.rede = rede;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEsferaAdministrativa() {
        return esferaAdministrativa;
    }

    public void setEsferaAdministrativa(String esferaAdministrativa) {
        this.esferaAdministrativa = esferaAdministrativa;
    }

    public String getCategoriaEscolaPrivada() {
        return categoriaEscolaPrivada;
    }

    public void setCategoriaEscolaPrivada(String categoriaEscolaPrivada) {
        this.categoriaEscolaPrivada = categoriaEscolaPrivada;
    }

    public String getSituacaoFuncionamento() {
        return situacaoFuncionamento;
    }

    public void setSituacaoFuncionamento(String situacaoFuncionamento) {
        this.situacaoFuncionamento = situacaoFuncionamento;
    }

    public String getSeFimLucrativo() {
        return seFimLucrativo;
    }

    public void setSeFimLucrativo(String seFimLucrativo) {
        this.seFimLucrativo = seFimLucrativo;
    }

    public String getSeConveniadaSetorPublico() {
        return seConveniadaSetorPublico;
    }

    public void setSeConveniadaSetorPublico(String seConveniadaSetorPublico) {
        this.seConveniadaSetorPublico = seConveniadaSetorPublico;
    }

    public Double getQtdSalasExistentes() {
        return qtdSalasExistentes;
    }

    public void setQtdSalasExistentes(Double qtdSalasExistentes) {
        this.qtdSalasExistentes = qtdSalasExistentes;
    }

    public Double getQtdSalasUtilizadas() {
        return qtdSalasUtilizadas;
    }

    public void setQtdSalasUtilizadas(Double qtdSalasUtilizadas) {
        this.qtdSalasUtilizadas = qtdSalasUtilizadas;
    }

    public Double getQtdFuncionarios() {
        return qtdFuncionarios;
    }

    public void setQtdFuncionarios(Double qtdFuncionarios) {
        this.qtdFuncionarios = qtdFuncionarios;
    }

    public Double getQtdComputadores() {
        return qtdComputadores;
    }

    public void setQtdComputadores(Double qtdComputadores) {
        this.qtdComputadores = qtdComputadores;
    }

    public Double getQtdComputadoresPorAluno() {
        return qtdComputadoresPorAluno;
    }

    public void setQtdComputadoresPorAluno(Double qtdComputadoresPorAluno) {
        this.qtdComputadoresPorAluno = qtdComputadoresPorAluno;
    }

    public Double getQtdAlunos() {
        return qtdAlunos;
    }

    public void setQtdAlunos(Double qtdAlunos) {
        this.qtdAlunos = qtdAlunos;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
