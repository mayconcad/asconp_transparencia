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

import com.mysql.fabric.xmlrpc.base.Array;

import br.com.asconp.asconp_transparencia.entities.DespesaReceita;
import br.com.asconp.asconp_transparencia.entities.GestorXml;
import br.com.asconp.asconp_transparencia.entities.LayoutXml;
import br.com.asconp.asconp_transparencia.entities.ReceitaCapital;
import br.com.asconp.asconp_transparencia.entities.ReceitaCorrente;
import br.com.asconp.asconp_transparencia.entities.ReceitaTable;
import br.com.asconp.asconp_transparencia.entities.ResumoReceita;
import br.com.asconp.asconp_transparencia.enums.EmpresaEnum;
import br.com.asconp.asconp_transparencia.enums.MesEnum;

@ManagedBean(name = "ParseReceitaForXmlController")
@ViewScoped
public class ParseReceitaForXmlController extends BaseController {

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;
	
	private String exercicio;

	private String fileNameExporter;

	// private ExcelOptions excelOpt;
	//
	// private PDFOptions pdfOpt;

	private boolean exibeMes;

	public List<LayoutXml> layoutXmlList;

	private List<ReceitaTable> receitas;
	
	
	public List<ReceitaTable> getReceitas() {
		return receitas;
	}

	public void setReceitas(List<ReceitaTable> receitas) {
		this.receitas = receitas;
	}

	public void buscar() {
		
		receitas=new ArrayList<ReceitaTable>();
		ReceitaTable receita=new ReceitaTable();
		List<ResumoReceita> resumos=new ArrayList<ResumoReceita>();
		ResumoReceita resumoReceita=new ResumoReceita();
		resumoReceita.setTipo("Novo Tipo");
		resumoReceita.setPercentualRealizado("20%");
		
		resumoReceita.setValorArrecadado("12000,00");
		resumoReceita.setValorOrcado("12000,00");
		
		resumos.add(resumoReceita);
		
		List<ReceitaCapital> receitasCap=new ArrayList<ReceitaCapital>();
		ReceitaCapital ReceitaCapital=new ReceitaCapital();
		ReceitaCapital.setDescricao("Novo Tipo 1");
		ReceitaCapital.setPercentualRealizado("20%");
		
		ReceitaCapital.setValorArrecadado("12000,00");
		ReceitaCapital.setValorOrcado("12000,00");
		
		receitasCap.add(ReceitaCapital);
		
		List<ReceitaCorrente> receitasCor=new ArrayList<ReceitaCorrente>();
		ReceitaCorrente ReceitaCorrente=new ReceitaCorrente();
		ReceitaCorrente.setDescricao("Novo Tipo 11");
		ReceitaCorrente.setPercentualRealizado("20%");
		
		ReceitaCorrente.setValorArrecadado("12000,00");
		ReceitaCorrente.setValorOrcado("12000,00");
		
		receitasCor.add(ReceitaCorrente);
		
		List<DespesaReceita> receitasRec=new ArrayList<DespesaReceita>();
		DespesaReceita DespesaReceita=new DespesaReceita();
		DespesaReceita.setDescricao("Novo Tipo 111");
		DespesaReceita.setPercentualRealizado("20%");
		
		DespesaReceita.setValorArrecadado("12000,00");
		DespesaReceita.setValorOrcado("12000,00");
		
		receitasRec.add(DespesaReceita);
		
		
		receita.setResumos(resumos);
		receita.setDespesasReceita(receitasRec);
		receita.setReceitasCapital(receitasCap);
		receita.setReceitasCorrentes(receitasCor);
		
		
		receitas.add(receita);
		
		
		
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
