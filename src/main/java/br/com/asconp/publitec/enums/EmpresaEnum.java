package br.com.asconp.publitec.enums;

public enum EmpresaEnum {
//nome da empresa e código da unidade gestora
	CM_PAJEU("Câmara Pajeu","101146"),
	CM_SAO_FELIX("Câmara São Félix","101184"),
	Luzilandia("Luzilândia","201120"),
	MANOEL_EMIDIO("Manoel Emídio","201122"),
	MATIAS_OLIMPIO("Matias Olímpio","201126"),	
	PauDarco("Pau D'arco","201154"),
	CM_TAMBORIL("Câmara Tamboril","101213"),
	CM_SAO_JOSE_PEIXE("Câmara São José do Peixe","101196"),
	CM_CANTO_BURITI("Câmara Canto do Buriti","101049"),
	CM_BREJO("Câmara Brejo","101036"),
	CM_PEDRO_LAURENTINO("Câmara Pedro Laurentino","101158"),
	CM_GERVAZIO_OLIVEIRA("Câmara Gervasio Oliveira","101051"),
	CM_RIBEIRA("Câmara Ribeira","101171"),
	CM_SAO_MIGUEL_FIDALGO("Câmara São Miguel do Fidalgo","101202");	
	
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
