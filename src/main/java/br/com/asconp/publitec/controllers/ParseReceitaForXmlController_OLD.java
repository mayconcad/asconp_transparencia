package br.com.asconp.publitec.controllers;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletContext;

import org.omnifaces.util.Ajax;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.asconp.publitec.entities.DespesaReceita;
import br.com.asconp.publitec.entities.LayoutXml;
import br.com.asconp.publitec.entities.ReceitaCapital;
import br.com.asconp.publitec.entities.ReceitaCorrente;
import br.com.asconp.publitec.entities.ReceitaTable;
import br.com.asconp.publitec.entities.ResumoReceita;
import br.com.asconp.publitec.enums.EmpresaEnum;
import br.com.asconp.publitec.enums.MesEnum;


@ManagedBean(name = "ParseReceitaForXmlController")
@ViewScoped
public class ParseReceitaForXmlController_OLD extends BaseController {

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;
	
	private String exercicio;

	private String fileNameExporter;
	
	public String codmunicipio="";
	
	private String linkVoltar;

	// private ExcelOptions excelOpt;
	//
	// private PDFOptions pdfOpt;

	private boolean exibeMes;

	public List<LayoutXml> layoutXmlList;

	private List<ReceitaTable> receitas;
	
	Calendar calDataAtual=Calendar.getInstance();
	
	@PostConstruct
	public void init(){		
		calDataAtual.setTime(new Date());
	}
	
	
	public List<ReceitaTable> getReceitas() {
		return receitas;
	}

	public void setReceitas(List<ReceitaTable> receitas) {
		this.receitas = receitas;
	}

	public void buscar() {
		
		receitas=new ArrayList<ReceitaTable>();
		ReceitaTable receita=new ReceitaTable();
		receita.setTitulo("Resumos");
		ReceitaTable receita2=new ReceitaTable();
		receita2.setTitulo("Receitas de Capital");
		ReceitaTable receita3=new ReceitaTable();
		receita3.setTitulo("Despesas da Receita");
		ReceitaTable receita4=new ReceitaTable();
		receita4.setTitulo("Receitas Correntes");
		
		List<ResumoReceita> resumos=new ArrayList<ResumoReceita>();
		List<ResumoReceita> resumos2=new ArrayList<ResumoReceita>();
		List<ResumoReceita> resumos3=new ArrayList<ResumoReceita>();
		List<ResumoReceita> resumos4=new ArrayList<ResumoReceita>();
		ResumoReceita resumoReceita=new ResumoReceita();
		resumoReceita.setDescricao("Novo Tipo");
		resumoReceita.setPercentualRealizado("10%");
		
		resumoReceita.setValorArrecadado("2000,00");
		resumoReceita.setValorOrcado("2000,00");
		
		ResumoReceita resumoReceita2=new ResumoReceita();
		resumoReceita2.setDescricao("Novo Tipo2");
		resumoReceita2.setPercentualRealizado("20%");
		
		resumoReceita2.setValorArrecadado("12000,00");
		resumoReceita2.setValorOrcado("12000,00");
		
		ResumoReceita resumoReceita3=new ResumoReceita();
		resumoReceita3.setDescricao("Novo Tipo3");
		resumoReceita3.setPercentualRealizado("30%");
		
		resumoReceita3.setValorArrecadado("3000,00");
		resumoReceita3.setValorOrcado("3000,00");
		
		ResumoReceita resumoReceita4=new ResumoReceita();
		resumoReceita4.setDescricao("Novo Tipo3");
		resumoReceita4.setPercentualRealizado("30%");
		
		resumoReceita4.setValorArrecadado("3000,00");
		resumoReceita4.setValorOrcado("3000,00");
		
		resumos.add(resumoReceita);
		resumos2.add(resumoReceita2);
		resumos3.add(resumoReceita3);
		resumos4.add(resumoReceita4);
		
		
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
		receita2.setResumos(resumos2);
		receita3.setResumos(resumos3);
		receita4.setResumos(resumos4);
		
		
		receita.setDespesasReceita(receitasRec);
		receita.setReceitasCapital(receitasCap);
		receita.setReceitasCorrentes(receitasCor);
		
		
		receitas.add(receita);
		receitas.add(receita2);
		receitas.add(receita3);
		receitas.add(receita4);
		
		setFileNameExporter("Receitas_ano_mes");
	}
	
	public String getNomeEmpresa(){
		switch(codmunicipio)
		{
		case "201120": {
			setEmpresaEnum(EmpresaEnum.Luzilandia);
			return EmpresaEnum.Luzilandia.toString();
		}
		case "101126": {
			setEmpresaEnum(EmpresaEnum.CM_MATIAS_OLIMPIO);
			return EmpresaEnum.CM_MATIAS_OLIMPIO.toString();
		}
		case "101146": {
			setEmpresaEnum(EmpresaEnum.CM_PAJEU);
			return EmpresaEnum.CM_PAJEU.toString();
		}
		case "101184": {
			setEmpresaEnum(EmpresaEnum.CM_SAO_FELIX);
			return EmpresaEnum.CM_SAO_FELIX.toString();}
		case "201122": {
			setEmpresaEnum(EmpresaEnum.MANOEL_EMIDIO);
			return EmpresaEnum.MANOEL_EMIDIO.toString();
		}
		case "201126": {
			setEmpresaEnum(EmpresaEnum.MATIAS_OLIMPIO);
			return EmpresaEnum.MATIAS_OLIMPIO.toString();
		}
		case "101213": {
			setEmpresaEnum(EmpresaEnum.CM_TAMBORIL);
			return EmpresaEnum.CM_TAMBORIL.toString();
		}
		
		case "101196": {
			setEmpresaEnum(EmpresaEnum.CM_SAO_JOSE_PEIXE);
			return EmpresaEnum.CM_SAO_JOSE_PEIXE.toString();
		}
		case "101049": {
			setEmpresaEnum(EmpresaEnum.CM_CANTO_BURITI);
			return EmpresaEnum.CM_CANTO_BURITI.toString();
		}
		case "101036": {
			setEmpresaEnum(EmpresaEnum.CM_BREJO);
			return EmpresaEnum.CM_BREJO.toString();
		}
		case "101158": {
			setEmpresaEnum(EmpresaEnum.CM_PEDRO_LAURENTINO);
			return EmpresaEnum.CM_PEDRO_LAURENTINO.toString();
		}
		case "101051": {
			setEmpresaEnum(EmpresaEnum.CM_GERVAZIO_OLIVEIRA);
			return EmpresaEnum.CM_GERVAZIO_OLIVEIRA.toString();
		}
		case "101171": {
			setEmpresaEnum(EmpresaEnum.CM_RIBEIRA);
			return EmpresaEnum.CM_RIBEIRA.toString();
		}
		case "101202": {
			setEmpresaEnum(EmpresaEnum.CM_SAO_MIGUEL_FIDALGO);
			return EmpresaEnum.CM_SAO_MIGUEL_FIDALGO.toString();
		}			
		
		default: {
			setEmpresaEnum(EmpresaEnum.PauDarco);
			return EmpresaEnum.PauDarco.toString();
		}
		
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
	
	
public String getCodmunicipio() {
		return codmunicipio;
	}


	public void setCodmunicipio(String codmunicipio) {
		this.codmunicipio = codmunicipio;
	}


public String[] getExercicios(){
		
		String caminho = System.getProperty("user.home").concat(File.separator).concat(codmunicipio).concat(File.separator);
		
		List<String> exercicios=new ArrayList<String>();
		
		for(int i = 2012; i <= calDataAtual.get(Calendar.YEAR); i++){
			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(i).append(File.separator);
			if (Paths.get(filePath.toString()).toFile().exists()) {				
				exercicios.add(i+"");
			}
		}
		String[] exers=new String[exercicios.size()];
		int x = 0;
		for (String ano : exercicios) {
			exers[x] = ano;
			x++;
		}
		
		return exers;
	}

	public EmpresaEnum[] getEmpresas() {

		if(exercicio == null || exercicio == "")
			return new EmpresaEnum[0];
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		List<EmpresaEnum> empresas = new ArrayList<EmpresaEnum>();
//		ServletContext servletContext = (ServletContext) FacesContext
//				.getCurrentInstance().getExternalContext().getContext();
//		String caminho = servletContext.getRealPath(File.separator);
		
		String caminho = System.getProperty("user.home").concat(File.separator);
		
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
		//Ajax.update("searchForm:mes");

		return emps;
	}

	public MesEnum[] getMeses() {
		
		if(exercicio == null || exercicio == "" || getEmpresaEnum() == null)
			return new MesEnum[0];
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		List<MesEnum> meses = new ArrayList<MesEnum>();
//		ServletContext servletContext = (ServletContext) FacesContext
//				.getCurrentInstance().getExternalContext().getContext();
//		String caminho = servletContext.getRealPath(File.separator);
		
		String caminho = System.getProperty("user.home").concat(File.separator).concat(codmunicipio).concat(File.separator);
//		
//		if(empresaEnum  ==null)
//			return new MesEnum[]{};
		for (MesEnum mes : MesEnum.values()) {
//
			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(cal.get(Calendar.YEAR))
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


	public String getLinkVoltar() {
		switch (codmunicipio) {
		case "201120": {
			return "http://www.publitecportais.org/portal_transparencia/luzilandia/index-old.html";
		}
		case "101126": {
			return "http://www.publitecportais.org/portal_transparencia/matiasolimpio/index.html";
		}
		case "101146": {
			return "http://www.publitecportais.org/portal_transparencia/pajeu/index.html";
		}
		case "101184": {
			return "http://www.publitecportais.org/portal_transparencia/sao_felix/index.html";
		}
		case "201122": {
			return "http://www.publitecportais.org/portal_transparencia/manoelemidio/index.html";
		}
		case "201126": {
			return "http://www.publitecportais.org/portal_transparencia/matiasolimpio/index.html";
		}
		case "101213": {
			return "http://www.publitecportais.org/portal_transparencia/tamboril/index.html";
		}
		case "101196": {
			return "http://www.publitecportais.org/portal_transparencia/sao_jose_do_peixe/index.html";
		}
		case "101049": {
			return "http://www.publitecportais.org/portal_transparencia/canto_do_buriti/index.html";
		}
		case "101036": {
			return "http://www.publitecportais.org/portal_transparencia/brejo/index.html";
		}
		case "101158": {
			return "http://www.publitecportais.org/portal_transparencia/pedro_laurentino/index.html";
		}
		case "101051": {
			return "http://www.publitecportais.org/portal_transparencia/gervasio_oliveira/index.html";
		}
		case "101171": {
			return "http://www.publitecportais.org/portal_transparencia/ribeira/index.html";
		}
		case "101202": {
			return "http://www.publitecportais.org/portal_transparencia/sao_miguel/index.html";
		}

		default: {
			return "#";
		}

		}


	}


	public void setLinkVoltar(String linkVoltar) {
		this.linkVoltar = linkVoltar;
	}
	

//	@Override
//	public void onTabChanged(TabChangeEvent event) {
//		// TODO Auto-generated method stub
//
//	}

}
