package br.com.asconp.asconp_transparencia.controllers;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.omnifaces.util.Ajax;
import org.primefaces.event.TabChangeEvent;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.asconp.asconp_transparencia.entities.GestorXml;
import br.com.asconp.asconp_transparencia.entities.LayoutXml;
import br.com.asconp.asconp_transparencia.enums.EmpresaEnum;
import br.com.asconp.asconp_transparencia.enums.MesEnum;

@ManagedBean(name = "ParseForXmlController")
@ViewScoped
public class ParseForXmlController extends BaseController {

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;
	
	private String exercicio;

	private String fileNameExporter;

	// private ExcelOptions excelOpt;
	//
	// private PDFOptions pdfOpt;

	private boolean exibeMes;

	public List<LayoutXml> layoutXmlList;

	public void buscar() {

		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		layoutXmlList = new ArrayList<LayoutXml>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			// String property = System.getProperty("user.home");
			// String dir =
			// System.getProperty("wtp.deploy")+"/asconp_transparencia";
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			String caminho = servletContext.getRealPath(File.separator);

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(cal.get(Calendar.YEAR))
			.append(File.separator).append(empresaEnum.getCodigo())
					.append(File.separator);

			filePath.append(String.format("%02d", mesEnum.ordinal() + 1))
					.append(File.separator);

			Document document = builder.parse(filePath.toString()
					+ "/EmpenhoseRP.xml");

			/*
			 * NodeList nodeList = document.getElementsByTagName(
			 * "aux:ConciliacaoBancaria" );
			 * 
			 * for ( int i = 0; i < nodeList.getLength(); i++ ) { Element
			 * element = (Element) nodeList.item( i ); LayoutXml layoutXml = new
			 * LayoutXml();
			 * 
			 * layoutXml.setCodigoBanco( element.getElementsByTagName(
			 * "aux:codigoBanco" ).item( 0 ).getTextContent() );
			 * layoutXml.setCodigoAgencia( element.getElementsByTagName(
			 * "aux:codigoAgencia" ).item( 0 ).getTextContent() );
			 * layoutXml.setNumeroConta( element.getElementsByTagName(
			 * "aux:numeroContBancaria" ).item( 0 ).getTextContent() );
			 * 
			 * layoutXml.setNumeroConta( element.getElementsByTagName(
			 * "aux:numeroContBancaria" ).item( 0 ).getTextContent() );
			 * layoutXml.setNumeroConta( element.getElementsByTagName(
			 * "aux:numeroContBancaria" ).item( 0 ).getTextContent() );
			 * 
			 * //NodeList childNodes = element.getChildNodes();
			 * 
			 * layoutXmlList.add( layoutXml ); }
			 */

			NodeList nodeList = document.getElementsByTagName("emp:Empenho");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				LayoutXml layoutXml = new LayoutXml();

				layoutXml.setNumeroEmpenho(element
						.getElementsByTagName("emp:numeroEmpenho").item(0)
						.getTextContent());
				layoutXml.setHistoricoEmpenho(element
						.getElementsByTagName("emp:historicoEmpenho")
						.item(0).getTextContent());
				
				try {
					Date date = sdf.parse(element
							.getElementsByTagName("emp:dataEmisEmpenho")
							.item(0).getTextContent());					 
					layoutXml.setDataEmisEmpenho(sdf2.format(date));
					
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				layoutXml.setCodigoUnidOrcamentaria(element
						.getElementsByTagName("emp:codigoUnidOrcamentaria")
						.item(0).getTextContent());
				layoutXml.setNomeUnidOrcamentaria(getNomeUnidadeGestora(document));
				
				String cpfCnpjCredor=element
						.getElementsByTagName("emp:cpfCnpjCredor")
						.item(0).getTextContent();
				layoutXml.setCpfCnpjCredor(getNomeCredor(builder.parse(filePath.toString()
						+ "/CadastrosAuxiliares.xml"),cpfCnpjCredor));
				
				
				layoutXml.setValorEmpenho(UtilsModel.convertBigDecimalToString(new BigDecimal(element
						.getElementsByTagName("emp:valorEmpenho")
						.item(0).getTextContent())));
				
//				NodeList childNodes = document
//						.getElementsByTagName("aux:numeroAtoQueNomeGestor");
//
//				List<GestorXml> gestores = new ArrayList<GestorXml>();
//
//				for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
//					Element element2 = (Element) childNodes.item(i2);
//					GestorXml gestor = new GestorXml();
//
//					gestor.setNumero(element2
//							.getElementsByTagName("gen:numero").item(0)
//							.getTextContent());
//					gestor.setAno(element2.getElementsByTagName("gen:ano")
//							.item(0).getTextContent());
//
//					gestores.add(gestor);
//				}
//
//				layoutXml.setGestores(gestores);
				if(layoutXml.getCpfCnpjCredor() != null && !layoutXml.getCpfCnpjCredor().isEmpty() )
				layoutXmlList.add(layoutXml);

				setFileNameExporter("Despesa_empresa_ano_mes");
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getNomeCredor(Document document, String cpfCnpjCredor) {
		
		String result="";
		NodeList nodeList = document.getElementsByTagName("aux:Fornecedor");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String cpfCnpjCred = element
					.getElementsByTagName("aux:cpfCnpjCredor").item(0)
					.getTextContent();
			if(cpfCnpjCred.equals(cpfCnpjCredor)){
				result=String.format("%s - %s", cpfCnpjCred,element
						.getElementsByTagName("aux:nomeDenoOuRazaJuridica").item(0)
						.getTextContent());
				break;
			}
		}
		
		return result;
	}

	private String getNomeUnidadeGestora(Document document) {
		NodeList nodeList = document.getElementsByTagName("emp:PrestacaoContas");
		String codUniGestora="";
		String nomeUnidGestora="";
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			 codUniGestora = element
			.getElementsByTagName("aux:codigoUnidGestora").item(0)
			.getTextContent();
			 nomeUnidGestora = element
			.getElementsByTagName("aux:nomeUnidGestora").item(0)
			.getTextContent();
		}
		return codUniGestora+"-"+nomeUnidGestora;
	}

	public void selectEmpresa(AjaxBehaviorEvent event) {
		if (empresaEnum == null) {
			setExibeMes(false);
		}else{
			setEmpresaEnum(empresaEnum);
		setExibeMes(true);
		}
	}

	public List<LayoutXml> getLayoutXmlList() {

		return layoutXmlList;
	}

	public void setLayoutXmlList(List<LayoutXml> layoutXmlList) {
		this.layoutXmlList = layoutXmlList;
	}

	public EmpresaEnum getEmpresaEnum() {
		return empresaEnum;
	}

	public void setEmpresaEnum(EmpresaEnum empresaEnum) {
		this.empresaEnum = empresaEnum;
	}

	public MesEnum getMesEnum() {
		return mesEnum;
	}

	public void setMesEnum(MesEnum mesEnum) {
		this.mesEnum = mesEnum;
	}

	public boolean getExibeMes() {
		return exibeMes;
	}

	public void setExibeMes(boolean exibeMes) {
		this.exibeMes = exibeMes;
	}
	
	public String[] getExercicios(){
		return new String[]{"2017","2016","2015","2014","2013","2012"};
	}

	public EmpresaEnum[] getEmpresas() {

		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		List<EmpresaEnum> empresas = new ArrayList<EmpresaEnum>();
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String caminho = servletContext.getRealPath(File.separator);
		for (EmpresaEnum emp : EmpresaEnum.values()) {

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(cal.get(Calendar.YEAR)).append(File.separator).append(emp.getCodigo())
					.append(File.separator);
			if (Paths.get(filePath.toString()).toFile().exists()) {
				empresas.add(emp);
			}
		}

		EmpresaEnum[] emps = new EmpresaEnum[empresas.size()];
		for (EmpresaEnum empresaEnum : empresas) {
			emps[empresas.indexOf(empresaEnum)] = empresaEnum;
		}
		Ajax.update("searchForm:mes");

		return emps;
	}

	public MesEnum[] getMeses() {
		if(getEmpresaEnum() == null)
		return MesEnum.values();
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		List<MesEnum> meses = new ArrayList<MesEnum>();
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String caminho = servletContext.getRealPath(File.separator);
//		
//		if(empresaEnum  ==null)
//			return new MesEnum[]{};
		for (MesEnum mes : MesEnum.values()) {
//
			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(cal.get(Calendar.YEAR)).append(File.separator).append(getEmpresaEnum().getCodigo())
					.append(File.separator);
			filePath.append(String.format("%02d", mes.ordinal() + 1)).append(
					File.separator);
			if (Paths.get(filePath.toString()).toFile().exists()) {
				meses.add(mes);
			}
		}
		MesEnum[] mess = new MesEnum[meses.size()];
		for (MesEnum mesEnum : meses) {
			mess[meses.indexOf(mesEnum)] = mesEnum;
		}

		return mess;
	}

	public String getFileNameExporter() {
		return fileNameExporter;
	}

	public void setFileNameExporter(String fileNameExporter) {
		this.fileNameExporter = fileNameExporter;
	}
	
	public String getExercicio() {
		return exercicio;
	}

	public void setExercicio(String exercicio) {
		this.exercicio = exercicio;
	}

	@Override
	public void onTabChanged(TabChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
