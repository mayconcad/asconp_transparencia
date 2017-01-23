package br.com.asconp.asconp_transparencia.enums;

public enum MesEnum {

	JAN("Janeiro"),	
	FEV("Fevereiro"),
	MAR("Março"),
	ABR("Abril"),
	MAI("Maio"),
	JUN("Junho"),
	JUL("Julho"),
	AGO("Agosto"),
	SET("Setembro"),
	OUT("Outubro"),
	NOV("Novembro"),
	DEZ("Dezembro");
	
	private String nome;

	private MesEnum(String valor){
		this.nome=valor;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}		
}
