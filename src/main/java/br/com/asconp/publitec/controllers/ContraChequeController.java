package br.com.asconp.publitec.controllers;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.asconp.publitec.dao.DAO;
import br.com.asconp.publitec.dao.DAOImpl;
import br.com.asconp.publitec.entities.ReceitaPessoal;
import br.com.asconp.publitec.enums.EmpresaEnum;
import br.com.asconp.publitec.enums.MesEnum;



@ManagedBean(name = "ContraChequeController")
@ViewScoped
public class ContraChequeController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1946000599223963021L;

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;

	private String exercicio;

	private String fileNameExporter;

	public String codmunicipio = "";
	
	private String linkVoltar;
	
	public String matricula;
	public String senha;
	
	private boolean exibeMes;

	public List<ReceitaPessoal> layoutXmlList;

	Calendar calDataAtual = Calendar.getInstance();
	
	DAO dao=null;

	@PostConstruct
	public void init() {
		calDataAtual.setTime(new Date());
		dao=new DAOImpl();
	
	}

	public ContraChequeController() {
		// TODO Auto-generated constructor stub
	}
	

	private String obterValorLancado(String codUnidOrcamentaria, List elementos) {

		String result = null;
		if (elementos.size() > 0) {

			Iterator it = elementos.iterator();

			while (it.hasNext()) {
				org.jdom2.Element item = (org.jdom2.Element) it.next();
				if ("LancamentoContabil".equals(item.getName())) {
					
											
						return item.getChild("LancamentoContabilItem").getChildText("valorLancado");
						
//						
//						if (child != null) {
//							result = child;
//						} else {
//
//							result = obterValorLancado(codUnidOrcamentaria,
//									item.getChildren());
//							if (result == null)
//								continue;
//						}
					}else{
						obterValorLancado(codUnidOrcamentaria, item.getChildren());
					}
				

			}
		}
		return result;
	}

	private void printDataNode(NodeList childNodes) {

		for (int x = 0; x < childNodes.getLength(); x++) {

			Node tempNode = childNodes.item(x);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name and value
				// lan:LancamentoContabilItem
				System.out.println("\nNOME DO NÓ =" + tempNode.getNodeName()
						+ " [TAG ABRE]");
				System.out.println("VALOR DO NÓ =" + tempNode.getTextContent());

				String codigoUnidOrcamentaria = "";
				if (tempNode.getNodeName().equals("lan:codigoUnidOrcamentaria")) {
					codigoUnidOrcamentaria = tempNode.getTextContent();
				}

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int y = 0; y < nodeMap.getLength(); y++) {

						Node node = nodeMap.item(y);
						System.out.println("attr nome : " + node.getNodeName());
						System.out.println("attr valor : "
								+ node.getNodeValue());

					}

				}

			}

			if (tempNode.hasChildNodes()) {

				// loop again if has child nodes
				printDataNode(tempNode.getChildNodes());

			}

			System.out.println("NOME DO NÓ =" + tempNode.getNodeName()
					+ " [ENCERRA TAG]");
			if (x == 2)
				break;
		}
	}

	private String getNomeCredor(Document document, String cpfCnpjCredor) {

		String result = "";
		NodeList nodeList = document.getElementsByTagName("aux:Fornecedor");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String cpfCnpjCred = element
					.getElementsByTagName("aux:cpfCnpjCredor").item(0)
					.getTextContent();
			if (cpfCnpjCred.equals(cpfCnpjCredor)) {
				result = String.format("%s - %s", cpfCnpjCred, element
						.getElementsByTagName("aux:nomeDenoOuRazaJuridica")
						.item(0).getTextContent());
				break;
			}
		}

		return result;
	}

	private String getValorPagoEmpenho(Document document) {
		NodeList nodeList = document
				.getElementsByTagName("emp:PrestacaoContas");
		String codUniGestora = "";
		String nomeUnidGestora = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			codUniGestora = element
					.getElementsByTagName("aux:codigoUnidGestora").item(0)
					.getTextContent();
			nomeUnidGestora = element
					.getElementsByTagName("aux:nomeUnidGestora").item(0)
					.getTextContent();
		}
		return codUniGestora + "-" + nomeUnidGestora;
	}

	private String getNomeUnidadeGestora(Document document) {
		NodeList nodeList = document
				.getElementsByTagName("emp:PrestacaoContas");
		String codUniGestora = "";
		String nomeUnidGestora = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			codUniGestora = element
					.getElementsByTagName("aux:codigoUnidGestora").item(0)
					.getTextContent();
			nomeUnidGestora = element
					.getElementsByTagName("aux:nomeUnidGestora").item(0)
					.getTextContent();
		}
		return codUniGestora + "-" + nomeUnidGestora;
	}

	public void selectEmpresa(AjaxBehaviorEvent event) {
		if (empresaEnum == null) {
			setExibeMes(false);
		} else {
			setEmpresaEnum(empresaEnum);
			setExibeMes(true);
		}
	}

	public List<ReceitaPessoal> getLayoutXmlList() {

		return layoutXmlList;
	}

	public void setLayoutXmlList(List<ReceitaPessoal> layoutXmlList) {
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

	public String[] getExercicios() {

		String caminho = System.getProperty("user.home").concat(File.separator).concat(codmunicipio).concat(File.separator);

		List<String> exercicios = new ArrayList<String>();

		for (int i = 2012; i <= calDataAtual.get(Calendar.YEAR); i++) {
			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(i).append(File.separator);
			if (Paths.get(filePath.toString()).toFile().exists()) {
				exercicios.add(i + "");
			}
		}
		String[] exers = new String[exercicios.size()];
		int x = 0;
		for (String ano : exercicios) {
			exers[x] = ano;
			x++;
		}

		return exers;
	}

	public EmpresaEnum[] getEmpresas() {

		if (exercicio == null || exercicio == "")
			return new EmpresaEnum[0];
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		List<EmpresaEnum> empresas = new ArrayList<EmpresaEnum>();
		// ServletContext servletContext = (ServletContext) FacesContext
		// .getCurrentInstance().getExternalContext().getContext();
		// String caminho = servletContext.getRealPath(File.separator);

		String caminho = System.getProperty("user.home").concat(File.separator);

		for (EmpresaEnum emp : EmpresaEnum.values()) {

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(cal.get(Calendar.YEAR))
					.append(File.separator).append(emp.getCodigo())
					.append(File.separator);
			if (Paths.get(filePath.toString()).toFile().exists()) {
				empresas.add(emp);
			}
		}

		EmpresaEnum[] emps = new EmpresaEnum[empresas.size()];
		for (EmpresaEnum empresaEnum : empresas) {
			emps[empresas.indexOf(empresaEnum)] = empresaEnum;
		}
		// Ajax.update("searchForm:mes");

		return emps;
	}

	public MesEnum[] getMeses() {

		//return MesEnum.values();
		if (exercicio == null || exercicio == "" || getEmpresaEnum() == null)
			return new MesEnum[0];

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		List<MesEnum> meses = new ArrayList<MesEnum>();
		// ServletContext servletContext = (ServletContext) FacesContext
		// .getCurrentInstance().getExternalContext().getContext();
		// String caminho = servletContext.getRealPath(File.separator);

		String caminho = System.getProperty("user.home").concat(File.separator)
				.concat(codmunicipio).concat(File.separator);
		//
		// if(empresaEnum ==null)
		// return new MesEnum[]{};
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

	public String getNomeEmpresa() {
		switch (codmunicipio) {
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

	public String getCodmunicipio() {
		return codmunicipio;
	}

	public void setCodmunicipio(String codmunicipio) {
		this.codmunicipio = codmunicipio;
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

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLinkVoltar() {
		switch (codmunicipio) {
		case "201120": {
			return "http://www.publitecportais.org/portal_transparencia/luzilandia/index-old.html";
		}
		case "101126": {
			return "http://www.publitecportais.org/portal_transparencia/cm_matias_olimpio/index.html";
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
	

	// @Override
	// public void onTabChanged(TabChangeEvent event) {
	// // TODO Auto-generated method stub
	//
	// }

}
