package br.com.asconp.publitec.vos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;



import br.com.asconp.publitec.controllers.UtilsModel;

public class FinancialLaunchingReportVO extends BaseVO {

	private static final long serialVersionUID = 6975569044158445410L;

	private String description;

	private String launchingDate;

	private String value;

	private String launchingType;

	private String launchingSituation;

	private String amountParcels;

	private List<CategoryReportVO> categories;

	private List<ParcelReportVO> parcels;

	public FinancialLaunchingReportVO() {
		this.description = "Lnaçamento";
		this.launchingDate = new SimpleDateFormat("dd/MM/yyyy").format( new Date());
		this.categories = converterCategoriesForVO();
		this.parcels = converterParcelsForVO();
		this.value = UtilsModel.convertBigDecimalToString(new BigDecimal(1000));
		this.amountParcels = String.valueOf(2);
		this.launchingType = "Financeiro";
		this.launchingSituation = "Aberta";
	}

	private List<CategoryReportVO> converterCategoriesForVO(
			) {
		List<CategoryReportVO> vos = new ArrayList<CategoryReportVO>();
		for (int i=0; i < 2; i++) {
			vos.add(new CategoryReportVO("Categoria "+i));
		}
		return vos;
	}

	private List<ParcelReportVO> converterParcelsForVO() {
		List<ParcelReportVO> vos = new ArrayList<ParcelReportVO>();
		for (int i=0; i < 2; i++) {
			vos.add(new ParcelReportVO(i));
		}
		return vos;
	}

	public String getAmountParcels() {
		return amountParcels;
	}

	public String getDescription() {
		return description;
	}

	public String getLaunchingDate() {
		return launchingDate;
	}

	public List<CategoryReportVO> getCategories() {
		return categories;
	}

	public String getValue() {
		return value;
	}

	public String getLaunchingType() {
		return launchingType;
	}

	public String getLaunchingSituation() {
		return launchingSituation;
	}

	public List<ParcelReportVO> getParcels() {
		return parcels;
	}
}
