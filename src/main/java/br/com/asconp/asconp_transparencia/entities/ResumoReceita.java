package br.com.asconp.asconp_transparencia.entities;

public class ResumoReceita {
	
	private String tipo;
	
	private String valorOrcado;
	
	private String valorArrecadado;
	
	private String percentualRealizado;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValorOrcado() {
		return valorOrcado;
	}

	public void setValorOrcado(String valorOrcado) {
		this.valorOrcado = valorOrcado;
	}

	public String getValorArrecadado() {
		return valorArrecadado;
	}

	public void setValorArrecadado(String valorArrecadado) {
		this.valorArrecadado = valorArrecadado;
	}

	public String getPercentualRealizado() {
		return percentualRealizado;
	}

	public void setPercentualRealizado(String percentualRealizado) {
		this.percentualRealizado = percentualRealizado;
	}
	
}
