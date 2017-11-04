package br.com.asconp.publitec.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pub_despesa")
public class LayoutXml extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public long id;
	@Column
	public String 
	//DAdos prefixo: emp:PrestacaoContas do arquivo EmpenhosRP.xml
	codigoUnidGestora;
	@Column
	public String nomeUnidGestora;
	@Column
	public String cpfContador;
	@Column
	public String cpfGestor;
	@Column
	public String anoReferencia;
	@Column
	public String mesReferencia;
	//Dados prefixo: emp:Empenho do arquivo EmpenhosRP.xml
	@Column
	public String numeroEmpenho;
	@Column
	public String historicoEmpenho;
	@Column
	public String dataEmisEmpenho;
	@Column
	public String codigoUnidOrcamentaria;
	@Column
	public String nomeUnidOrcamentaria;
	@Column
	public String cpfCnpjCredor;
	@Column
	public String nomeCredor;
	@Column
	public String valorEmpenho;
	//Dados prefixo: lan:LancamentoContabil do arquivo LancamentosContabeis.xml
	@Column
	public String valorLiquidado;
	@Column
	public String valorPago;
	
	//private List<GestorXml> gestores;
	
	

	public String getCodigoUnidGestora() {
		return codigoUnidGestora;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

//	public List<GestorXml> getGestores() {
//		return gestores;
//	}
//
//	public void setGestores(List<GestorXml> gestores) {
//		this.gestores = gestores;
//	}

	
}
