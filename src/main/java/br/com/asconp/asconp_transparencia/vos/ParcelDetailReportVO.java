package br.com.asconp.asconp_transparencia.vos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.asconp.asconp_transparencia.controllers.UtilsModel;
import br.com.asconp.asconp_transparencia.enums.LaunchingType;

public class ParcelDetailReportVO extends BaseVO {

	private static final long serialVersionUID = 2314425572466478134L;
	
	private String user;

	private String expirationDate;

	private String descriptionLaunching;

	private String parcelType;

	private String parcelValue;

	private String parcelSituation;

	public ParcelDetailReportVO() {
		// TODO Auto-generated constructor stub
	}
	
	public ParcelDetailReportVO( int i ) {
		this.user = "Novo Usuário";
		this.expirationDate = new SimpleDateFormat( "dd/MM/yyyy" ).format( new Date() );
		this.descriptionLaunching = "Lançamento Teste no Detalhe";
		this.parcelType = LaunchingType.CREDIT.getDescription();
		this.parcelValue = UtilsModel.convertBigDecimalToString(new BigDecimal(150));
		this.parcelSituation = "Aberta";
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate( String expirationDate ) {
		this.expirationDate = expirationDate;
	}

	public String getDescriptionLaunching() {
		return descriptionLaunching;
	}

	public void setDescriptionLaunching( String descriptionLaunching ) {
		this.descriptionLaunching = descriptionLaunching;
	}

	public String getParcelType() {
		return parcelType;
	}

	public void setParcelType( String parcelType ) {
		this.parcelType = parcelType;
	}

	public String getParcelValue() {
		return parcelValue;
	}

	public void setParcelValue( String parcelValue ) {
		this.parcelValue = parcelValue;
	}

	public String getParcelSituation() {
		return parcelSituation;
	}

	public void setParcelSituation( String parcelSituation ) {
		this.parcelSituation = parcelSituation;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
