package br.com.asconp.publitec.vos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.asconp.publitec.controllers.UtilsModel;

public class ParcelReportVO extends BaseVO {

	private static final long serialVersionUID = 4439027125782852493L;

	private String date;

	private String value;

	private String parcelSituation;

	public ParcelReportVO() {
		// TODO Auto-generated constructor stub
	}

	public ParcelReportVO(int i) {
		this.date = new SimpleDateFormat( "dd/MM/yyyy" ).format( new Date() );
		this.value = UtilsModel.convertBigDecimalToString(new BigDecimal(500) );
		this.parcelSituation = "parcela "+i;
	}

	public String getDate() {
		return date;
	}

	public void setDate( String date ) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}

	public String getParcelSituation() {
		return parcelSituation;
	}

	public void setParcelSituation( String parcelSituation ) {
		this.parcelSituation = parcelSituation;
	}

}
