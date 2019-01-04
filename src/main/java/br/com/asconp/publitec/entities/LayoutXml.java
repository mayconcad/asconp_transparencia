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
	private long id;
	@Column
	private String 
	//DAdos prefixo: emp:PrestacaoContas do arquivo EmpenhosRP.xml
	codigoUnidGestora;
	@Column
	private String nomeUnidGestora;
	@Column
	private String cpfContador;
	@Column
	private String cpfGestor;
	@Column
	private String anoReferencia;
	@Column
	private String mesReferencia;
	//Dados prefixo: emp:Empenho do arquivo EmpenhosRP.xml
	@Column
	private String numeroEmpenho;
	@Column
	private String historicoEmpenho;
	@Column
	private String dataEmisEmpenho;
	@Column
	private String codigoUnidOrcamentaria;
	@Column
	private String nomeUnidOrcamentaria;
	@Column
	private String cpfCnpjCredor;
	@Column
	private String nomeCredor;
	@Column
	private String valorEmpenho;
	//Dados prefixo: lan:LancamentoContabil do arquivo LancamentosContabeis.xml
	@Column
	private String valorLiquidado;
	@Column
	private String valorPago;
	
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

	
}
