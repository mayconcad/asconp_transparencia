package br.com.asconp.asconp_transparencia.entities;

import java.util.List;

public class LayoutReceitaXml {

	private String 
	//DAdos prefixo: emp:PrestacaoContas do arquivo EmpenhosRP.xml
	codigoUnidGestora,nomeUnidGestora,cpfContador,cpfGestor,anoReferencia,mesReferencia,
	//Dados prefixo: emp:Empenho do arquivo EmpenhosRP.xml
	numeroEmpenho,historicoEmpenho,dataEmisEmpenho,codigoUnidOrcamentaria,nomeUnidOrcamentaria,cpfCnpjCredor, nomeCredor,valorEmpenho,
	//Dados prefixo: lan:LancamentoContabil do arquivo LancamentosContabeis.xml
	valorLiquidado, valorPago;
	

	public String getCodigoUnidGestora() {
		return codigoUnidGestora;
	}

	public void setCodigoUnidGestora(String codigoUnidGestora) {
		this.codigoUnidGestora = codigoUnidGestora;
	}

	public String getNomeUnidGestora() {
		return nomeUnidGestora;
	}

	public void setNomeUnidGestora(String nomeUnidGestora) {
		this.nomeUnidGestora = nomeUnidGestora;
	}

	public String getCpfContador() {
		return cpfContador;
	}

	public void setCpfContador(String cpfContador) {
		this.cpfContador = cpfContador;
	}

	public String getCpfGestor() {
		return cpfGestor;
	}

	public void setCpfGestor(String cpfGestor) {
		this.cpfGestor = cpfGestor;
	}

	public String getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(String anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public String getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public String getNumeroEmpenho() {
		return numeroEmpenho;
	}

	public void setNumeroEmpenho(String numeroEmpenho) {
		this.numeroEmpenho = numeroEmpenho;
	}

	public String getHistoricoEmpenho() {
		return historicoEmpenho;
	}

	public void setHistoricoEmpenho(String historicoEmpenho) {
		this.historicoEmpenho = historicoEmpenho;
	}

	public String getDataEmisEmpenho() {
		return dataEmisEmpenho;
	}

	public void setDataEmisEmpenho(String dataEmisEmpenho) {
		this.dataEmisEmpenho = dataEmisEmpenho;
	}

	public String getNomeUnidOrcamentaria() {
		return nomeUnidOrcamentaria;
	}

	public void setNomeUnidOrcamentaria(String nomeUnidOrcamentaria) {
		this.nomeUnidOrcamentaria = nomeUnidOrcamentaria;
	}

	public String getCpfCnpjCredor() {
		return cpfCnpjCredor;
	}

	public void setCpfCnpjCredor(String cpfCnpjCredor) {
		this.cpfCnpjCredor = cpfCnpjCredor;
	}

	public String getNomeCredor() {
		return nomeCredor;
	}

	public void setNomeCredor(String nomeCredor) {
		this.nomeCredor = nomeCredor;
	}

	public String getValorEmpenho() {
		return valorEmpenho;
	}

	public void setValorEmpenho(String valorEmpenho) {
		this.valorEmpenho = valorEmpenho;
	}

	public String getValorLiquidado() {
		return valorLiquidado;
	}

	public void setValorLiquidado(String valorLiquidado) {
		this.valorLiquidado = valorLiquidado;
	}

	public String getValorPago() {
		return valorPago;
	}

	public void setValorPago(String valorPago) {
		this.valorPago = valorPago;
	}

	public String getCodigoUnidOrcamentaria() {
		return codigoUnidOrcamentaria;
	}

	public void setCodigoUnidOrcamentaria(String codigoUnidOrcamentaria) {
		this.codigoUnidOrcamentaria = codigoUnidOrcamentaria;
	}
	
}
