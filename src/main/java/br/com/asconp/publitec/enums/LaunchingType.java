package br.com.asconp.publitec.enums;

public enum LaunchingType {
	DEBIT( "Débito" ),
	CREDIT( "Crédito" );

	private String description;

	private LaunchingType( String description ) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}

}
