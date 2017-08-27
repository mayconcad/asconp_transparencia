package br.com.asconp.publitec.controllers;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.asconp.publitec.vos.FinancialLaunchingReportVO;
import br.com.asconp.publitec.vos.ParcelDetailReportVO;
import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;

//import com.google.common.collect.Sets;

@ManagedBean
@ViewScoped
public class FinancialLaunchingReportController extends BaseReportController {

	private static final long serialVersionUID = 7416053011706409698L;

//	@ManagedProperty( "#{financialLaunchingService}" )
//	FinancialLaunchingService financialLaunchingService;
	
//	@ManagedProperty("#{userServiceImpl}")
//	  private UserService userService;
//
//	private FinancialLaunching financialLaunching;
//
//	private Category category;

	private Date initialLaunchingDate;

	private Date finalLaunchingDate;

	private String initialValue;

	private String finalValue;

	private boolean detailed;

	private boolean emptyReport;

	public FinancialLaunchingReportController() {
//		this.financialLaunching = new FinancialLaunching();
//		this.category = new Category();
		detailed = true;
	}

	public void generateDynamicReport() {
		
		BigDecimal initialDecimalValue =  new BigDecimal(1500 );
		BigDecimal finalDecimalValue = new BigDecimal( 1000);

//		financialLaunching.setCategories( Sets.newHashSet( category ) );
//		List<FinancialLaunching> launchings = financialLaunchingService.findForReport( financialLaunching, initialLaunchingDate, finalLaunchingDate, initialDecimalValue, finalDecimalValue );
//		if ( !hasValue( launchings ) ) {
//			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( "Não há registros para o(s) filtro(s) informado(s) !" ) );
//			emptyReport = true;
//			clearFields();
//			return;
//		}

		try {

			Style styleTitle = new Style();
			styleTitle.setBlankWhenNull( true );
			styleTitle.setFont( Font.ARIAL_BIG_BOLD );
			styleTitle.setName( "styleTitle" );
			styleTitle.setBorder( Border.PEN_1_POINT() );
			styleTitle.setTextColor( Color.blue );
			styleTitle.setTransparency( Transparency.OPAQUE );
			styleTitle.setStretchWithOverflow( true );
			styleTitle.setVerticalAlign( VerticalAlign.MIDDLE );

			Style styleColumn = new Style();
			styleColumn.setBlankWhenNull( true );
			styleColumn.setFont( Font.ARIAL_BIG_BOLD );
			styleColumn.setName( "styleColumn" );
			styleColumn.setTransparency( Transparency.OPAQUE );
			styleColumn.setStretchWithOverflow( true );
			styleColumn.setVerticalAlign( VerticalAlign.MIDDLE );

			FastReportBuilder reportBuilder = new FastReportBuilder();
			try {
				reportBuilder.addColumn( "Descrição", "description", String.class.getName(), 50 ).addColumn( "Vencimento", "launchingDate", String.class.getName(), 30 ).addColumn( "Valor", "value", String.class.getName(), 30 ).addColumn( "Tipo", "launchingType", String.class.getName(), 30 ).addColumn( "Situação", "launchingSituation", String.class.getName(), 30 ).setMargins( 5, 5, 20, 20 ).setTitle( "Relatório de Lançamentos Financeiros" ).setSubtitle( "Data: " + new SimpleDateFormat( "dd/MM/yyyy" ).format( new Date() ) ).setPrintColumnNames( true ).setUseFullPageWidth( true ).addAutoText( AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_CENTER );

				reportBuilder.addConcatenatedReport( createParcelsHeaderSubreport(), new ClassicLayoutManager(), "parcels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true );
				reportBuilder.addConcatenatedReport( createCategoriesHeaderSubreport(), new ClassicLayoutManager(), "categories", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true );

				reportBuilder.addField( "categories", List.class.getName() );
				reportBuilder.addField( "parcels", List.class.getName() );
			} catch ( ColumnBuilderException e ) {
				e.printStackTrace();
			} catch ( ClassNotFoundException e ) {
				e.printStackTrace();
			} catch ( DJBuilderException e ) {
				e.printStackTrace();
			}
			reportBuilder.setUseFullPageWidth( true );

			dynamicReport = reportBuilder.build();

			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put( "user", "Maycon Francis" );

			session.setAttribute( "dataList", converterLaunchingForVO() );
			session.setAttribute( "dynamicReport", dynamicReport );
			session.setAttribute( "reportType", reportType );
			session.setAttribute( "parameters", parameters );

			redirect( "/ReportData" );
		} catch ( Exception ex ) {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage() ) );
		}
	}

	private DynamicReport createCategoriesHeaderSubreport() throws ColumnBuilderException, ClassNotFoundException {
		Style titleStyle = new Style( "styleTitleSubReport" );
		titleStyle.setTextColor( Color.blue );
		titleStyle.setBackgroundColor( Color.gray );
		titleStyle.setBorder( Border.PEN_2_POINT() );
		titleStyle.setFont( new Font( 12, "Arial", true ) );
		titleStyle.setStretchWithOverflow( true );
		FastReportBuilder reportBuilder = new FastReportBuilder();
		reportBuilder.addColumn( "Nome", "name", String.class.getName(), 30 ).addGroups( 1 ).setMargins( 5, 5, 20, 20 ).setUseFullPageWidth( true ).setWhenNoDataNoPages().setTitle( "Categorias" ).setTitleStyle( titleStyle );
		return reportBuilder.build();
	}

	private DynamicReport createParcelsHeaderSubreport() throws ColumnBuilderException, ClassNotFoundException {
		Style titleStyle = new Style( "styleTitleSubReport" );
		titleStyle.setTextColor( Color.blue );
		titleStyle.setBackgroundColor( Color.gray );
		titleStyle.setBorder( Border.PEN_2_POINT() );
		titleStyle.setFont( new Font( 12, "Arial", true ) );
		titleStyle.setStretchWithOverflow( true );
		FastReportBuilder reportBuilder = new FastReportBuilder();
		reportBuilder.addColumn( "Valor", "parcelValue", String.class.getName(), 30 ).addColumn( "Situação", "parcelSituation", String.class.getName(), 30 ).addColumn( "Vencimento", "expirationDate", String.class.getName(), 30 ).setMargins( 5, 5, 20, 20 ).setUseFullPageWidth( true ).setWhenNoDataNoPages().setTitle( "Parcelas" ).setTitleStyle( titleStyle );
		return reportBuilder.build();
	}

	private List<FinancialLaunchingReportVO> converterLaunchingForVO() {
		List<FinancialLaunchingReportVO> vos = new ArrayList<FinancialLaunchingReportVO>();
		
		for ( int i=0; i < 2; i++ ) {
			vos.add( new FinancialLaunchingReportVO() );
		}
		return vos;
	}

	public void generateReportByTemplate() {

		BigDecimal initialDecimalValue = new BigDecimal(80 );
		BigDecimal finalDecimalValue = new BigDecimal( 200);

//		financialLaunching.setCategories( Sets.newHashSet( category ) );
		if ( detailed ) {
//			List<FinancialLaunching> launchings = financialLaunchingService.findForReport( financialLaunching, initialLaunchingDate, finalLaunchingDate, initialDecimalValue, finalDecimalValue );
			session.setAttribute( "dataList", converterLaunchingForVO( ) );
			session.setAttribute( "reportFileName", "FinancialLaunchingReport.jasper" );
		} else {
//			List<Parcel> findParcelsForReport = financialLaunchingService.findParcelsForReport( financialLaunching, initialLaunchingDate, finalLaunchingDate, initialDecimalValue, finalDecimalValue );
			session.setAttribute( "dataList", converterParcelsForVO() );
			session.setAttribute( "reportFileName", "ParcelsReport.jasper" );
		}
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		String username = "Novo Usuário";
		parameters.put( "user", username);
		parameters.put( "companyDescription", "Empresa Faker - 008.224.852/0001-12" );

		session.setAttribute( "logo", "logo_company.png" );
		session.setAttribute( "reportType", getReportType() );
		session.setAttribute( "parameters", parameters );
		session.setAttribute( "reportName", "Contracheque" );

		redirect( "/ReportData" );
		clearFields();
	}

	private List<ParcelDetailReportVO> converterParcelsForVO() {

		List<ParcelDetailReportVO> vos = new ArrayList<ParcelDetailReportVO>();
		
//		Map<Long, Set<Parcel>> launchingParcelMap = new HashMap<Long, Set<Parcel>>();
		for ( int i=0; i < 2; i++ ) {
//			if ( launchingParcelMap.containsKey( parcel.getFinancialLaunching().getId() ) ) {
//				Set<Parcel> parcels = launchingParcelMap.get( parcel.getFinancialLaunching().getId() );
//				parcels.add( parcel );
				vos.add( new ParcelDetailReportVO(i));
//			} else {
//				Set<Parcel> parcels = new HashSet<Parcel>();
//				parcels.add( parcel );
//				launchingParcelMap.put( parcel.getFinancialLaunching().getId(), parcels );
//				if (parcel.getFinancialLaunching().getParcels().size() > 1) {
//				  vos.add( new ParcelDetailReportVO( parcel, String.format( "%s/%s", parcels.size(), parcel.getFinancialLaunching().getParcels().size() ) ) );
//                }else {
//                  vos.add( new ParcelDetailReportVO( parcel , "" ) );
//                }
//			}
		}
		return vos;
	}

	private void clearFields() {
		
		this.initialLaunchingDate = null;
		this.finalLaunchingDate = null;
		this.initialValue = null;
		this.finalValue = null;
		this.detailed = true;
	}	

	public Date getInitialLaunchingDate() {
		return initialLaunchingDate;
	}

	public void setInitialLaunchingDate( Date initialLaunchingDate ) {
		this.initialLaunchingDate = initialLaunchingDate;
	}

	public Date getFinalLaunchingDate() {
		return finalLaunchingDate;
	}

	public void setFinalLaunchingDate( Date finalLaunchingDate ) {
		this.finalLaunchingDate = finalLaunchingDate;
	}

	public boolean getDetailed() {
		return detailed;
	}

	public void setDetailed( boolean detailed ) {
		this.detailed = detailed;
	}

	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue( String initialValue ) {
		this.initialValue = initialValue;
	}

	public String getFinalValue() {
		return finalValue;
	}

	public void setFinalValue( String finalValue ) {
		this.finalValue = finalValue;
	}

	public boolean isEmptyReport() {
		return emptyReport;
	}

	public void setEmptyReport( boolean emptyReport ) {
		this.emptyReport = emptyReport;
	}
}
