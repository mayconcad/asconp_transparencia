package br.com.asconp.publitec.entities;

import java.io.Serializable;

public class DemonstrativoReceita implements Serializable {
	
	private String codigo;
	
	private String titulo;
	
	private String receitaPrevista;
	
	private String arrecadacaoMes;
	
	private String arrecadacaoAcumulada;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		if("9".equals(codigo.substring(0, 1)))
			codigo = String.format("%s.%s.%s", codigo.substring(0, 5),codigo.substring(5, 7),codigo.substring(7, 9));
		else
			codigo = String.format("%s.%s.%s", codigo.substring(0, 4),codigo.substring(4, 6),codigo.substring(6, 8));
		this.codigo = codigo;
	}

	public String getReceitaPrevista() {
		return receitaPrevista;
	}

	public void setReceitaPrevista(String receitaPrevista) {
		this.receitaPrevista = receitaPrevista;
	}

	public String getArrecadacaoMes() {
		return arrecadacaoMes;
	}

	public void setArrecadacaoMes(String arrecadacaoMes) {
		this.arrecadacaoMes = arrecadacaoMes;
	}

	public String getArrecadacaoAcumulada() {
		return arrecadacaoAcumulada;
	}

	public void setArrecadacaoAcumulada(String arrecadacaoAcumulada) {
		this.arrecadacaoAcumulada = arrecadacaoAcumulada;
	}	
}
