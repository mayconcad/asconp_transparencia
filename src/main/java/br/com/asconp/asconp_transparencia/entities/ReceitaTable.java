package br.com.asconp.asconp_transparencia.entities;

import java.util.List;

public class ReceitaTable {
	
	private List<ResumoReceita> resumos;
	
	private List<ReceitaCapital> receitasCapital;
	
	private List<DespesaReceita> despesasReceita;
	
	private List<ReceitaCorrente> receitasCorrentes; 
	

	public List<ResumoReceita> getResumos() {
		return resumos;
	}

	public void setResumos(List<ResumoReceita> resumos) {
		this.resumos = resumos;
	}

	public List<ReceitaCapital> getReceitasCapital() {
		return receitasCapital;
	}

	public void setReceitasCapital(List<ReceitaCapital> receitasCapital) {
		this.receitasCapital = receitasCapital;
	}

	public List<DespesaReceita> getDespesasReceita() {
		return despesasReceita;
	}

	public void setDespesasReceita(List<DespesaReceita> despesasReceita) {
		this.despesasReceita = despesasReceita;
	}

	public List<ReceitaCorrente> getReceitasCorrentes() {
		return receitasCorrentes;
	}

	public void setReceitasCorrentes(List<ReceitaCorrente> receitasCorrentes) {
		this.receitasCorrentes = receitasCorrentes;
	}


}
