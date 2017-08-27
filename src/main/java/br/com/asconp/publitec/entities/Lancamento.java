package br.com.asconp.publitec.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

//indica que a classe irá gera uma tabela no banco de dados
@Entity
public class Lancamento {

	@Id
	@GeneratedValue
	private Integer id;

	@Size( max = 100 )
	@NotBlank
	private String descricao;

	@ManyToOne( optional = false )
	@JoinColumn
	@NotNull
	private Pessoa pessoa;

	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date dataVencimento;

	@Temporal( TemporalType.DATE )
	@NotNull
	private Date dataBaixa;

	@NotNull
	@Column( precision = 10, scale = 2 )
	private BigDecimal valor;

	@Enumerated( EnumType.STRING )
	@NotNull
	private TipoLancamento tipo;

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao( String descricao ) {
		this.descricao = descricao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa( Pessoa pessoa ) {
		this.pessoa = pessoa;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento( Date dataVencimento ) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataBaixa() {
		return dataBaixa;
	}

	public void setDataBaixa( Date dataBaixa ) {
		this.dataBaixa = dataBaixa;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor( BigDecimal valor ) {
		this.valor = valor;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo( TipoLancamento tipo ) {
		this.tipo = tipo;
	}
}
