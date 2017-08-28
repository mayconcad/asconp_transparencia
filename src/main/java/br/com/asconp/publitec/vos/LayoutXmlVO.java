package br.com.asconp.publitec.vos;

import br.com.asconp.publitec.entities.LayoutXml;

public class LayoutXmlVO extends BaseVO{
	

	private String 
	//DAdos prefixo: emp:PrestacaoContas do arquivo EmpenhosRP.xml
	codigoUnidGestora;
	private String nomeUnidGestora;
	private String cpfContador;
	private String cpfGestor;
	private String anoReferencia;
	private String mesReferencia;
	//Dados prefixo: emp:Empenho do arquivo EmpenhosRP.xml
	private String numeroEmpenho;
	private String historicoEmpenho;
	private String dataEmisEmpenho;
	private String codigoUnidOrcamentaria;
	private String nomeUnidOrcamentaria;
	private String cpfCnpjCredor;
	private String nomeCredor;
	private String valorEmpenho;
	//Dados prefixo: lan:LancamentoContabil do arquivo LancamentosContabeis.xml
	private String valorLiquidado;
	private String valorPago;	
	
	

	public LayoutXmlVO(LayoutXml entity) {

		this.codigoUnidGestora = entity.getCodigoUnidGestora();
		this.nomeUnidGestora = entity.getNomeUnidGestora();
		this.cpfContador = entity.getCpfContador();
		this.cpfGestor = entity.getCpfGestor();
		this.anoReferencia = entity.getAnoReferencia();
		this.mesReferencia = entity.getMesReferencia();
		this.numeroEmpenho = entity.getNumeroEmpenho();
		this.historicoEmpenho = entity.getHistoricoEmpenho();
		this.dataEmisEmpenho = entity.getDataEmisEmpenho();
		this.codigoUnidOrcamentaria = entity.getCodigoUnidOrcamentaria();
		this.nomeUnidOrcamentaria = entity.getNomeUnidOrcamentaria();
		this.cpfCnpjCredor = entity.getCpfCnpjCredor();
		this.nomeCredor = entity.getNomeCredor();
		this.valorEmpenho = entity.getValorEmpenho();
		this.valorLiquidado = entity.getValorLiquidado();
		this.valorPago = entity.getValorPago();
	}

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
