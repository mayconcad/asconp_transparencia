package br.com.asconp.publitec.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pub_receita_pessoal")
public class ReceitaPessoal extends BaseEntity {

	// private static final long serialVersionUID = -6736734717512375671L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	public long id;

	@Column
	public String cpf;

	@Column
	public String nome;

	@Column
	public String lotacao;

	@Column
	public String cargo;

	@Column
	public String remuneracao;

	@Column
	public String numunidadegestora;

	@Column
	public int ano;

	@Column
	public String mes;

	// private String cpfLink;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// public String getCpfLink() {
	// return cpfLink;
	// }
	//
	// public void setCpfLink(String cpfLink) {
	// this.cpfLink = cpfLink;
	// }

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if(nome == null)
			this.nome="";
		this.nome = nome;
	}

	public String getLotacao() {
		return lotacao;
	}

	public void setLotacao(String lotacao) {
		this.lotacao = lotacao;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		if (cargo == null)
			this.cargo = "";
		this.cargo = cargo;
	}

	public String getRemuneracao() {
		return remuneracao;
	}

	public void setRemuneracao(String remuneracao) {
		this.remuneracao = remuneracao;
	}

	public String getNumunidadegestora() {
		return numunidadegestora;
	}

	public void setNumunidadegestora(String numunidadegestora) {
		this.numunidadegestora = numunidadegestora;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	// public long getSerialversionuid() {
	// return serialVersionUID;
	// }

}
