package br.com.asconp.asconp_transparencia.enums;

public enum EmpresaEnum {
//nome da empresa e código da unidade gestora
	Luzilandia("Luzilândia","201120"),	
	PauDarco("Pau D'arco","201154");	
	
	private String nome;
	
	private String codigo; 

	private EmpresaEnum(String valor, String codigo){
		this.nome=valor;
		this.codigo=codigo;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}
	
	public String getCodigo(){
		return this.codigo;
	}
}
