package br.com.asconp.publitec.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.asconp.publitec.dao.DAO;
import br.com.asconp.publitec.dao.DAOImpl;
import br.com.asconp.publitec.entities.LayoutXml;
import br.com.asconp.publitec.entities.ReceitaPessoal;
import br.com.asconp.publitec.enums.EmpresaEnum;
import br.com.asconp.publitec.enums.MesEnum;
import br.com.asconp.publitec.vos.ReceitaPessoalVO;

@ManagedBean(name = "ReceitaPessoalController")
@ViewScoped
public class ReceitaPessoalController extends BaseController {

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;

	private String exercicio;

	private String fileNameExporter;

	public String codmunicipio = "";
	
	private String linkVoltar;
	
	public String nomeCPF;
	public String cargo;
	
	private boolean exibeMes;

	public List<ReceitaPessoalVO> layoutXmlList;

	Calendar calDataAtual = Calendar.getInstance();
	
	DAO dao=null;

	@PostConstruct
	public void init() {
		calDataAtual.setTime(new Date());
		dao=new DAOImpl();
	
	}

	public ReceitaPessoalController() {
		// TODO Auto-generated constructor stub
	}

	public void buscar() {
		
		
		
		if(UtilsModel.hasValue( getNomeCPF()) && UtilsModel.hasValue(getCargo()))
			layoutXmlList = dao.find(ReceitaPessoal.class, ReceitaPessoalVO.class, " x.numunidadegestora = '"+codmunicipio+"' AND x.ano = '"+getExercicio()+"' AND x.cargo like '%"+getCargo()+"%' AND (x.nome like '%"+getNomeCPF()+"%' OR x.cpf like '%"+getNomeCPF()+"%')");
		else if(UtilsModel.hasValue( getNomeCPF()))
			layoutXmlList = dao.find(ReceitaPessoal.class, ReceitaPessoalVO.class, " x.numunidadegestora = '"+codmunicipio+"' AND x.ano = '"+getExercicio()+"' AND x.nome like '%"+getNomeCPF()+"%' OR x.cpf like '%"+getNomeCPF()+"%'");
		else if(UtilsModel.hasValue(getCargo()))
			layoutXmlList = dao.find(ReceitaPessoal.class, ReceitaPessoalVO.class, " x.numunidadegestora = '"+codmunicipio+"' AND x.ano = '"+getExercicio()+"' AND x.cargo like '%"+getCargo()+"%'");
		else
			layoutXmlList = dao.find(ReceitaPessoal.class, ReceitaPessoalVO.class, " x.numunidadegestora = '"+codmunicipio+"' AND x.ano = '"+getExercicio()+"'");

		/*Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		layoutXmlList = new ArrayList<ReceitaPessoalVO>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();


			String caminho = System.getProperty("user.home").concat(
					File.separator);

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(empresaEnum.getCodigo())
					.append(File.separator).append(cal.get(Calendar.YEAR))
					.append(File.separator);

			filePath.append(String.format("%02d", mesEnum.ordinal() + 1))
					.append(File.separator);

			Document document = builder.parse(filePath.toString()
					+ "/FolhaPagamento.xml");

		

			NodeList nodeList = document.getElementsByTagName("FolhaPagamento");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				ReceitaPessoal layoutXml = new ReceitaPessoal();

				String cpf = element
						.getElementsByTagName("cpf").item(0)
						.getTextContent();

				layoutXml.setCpf(cpf);
				layoutXml.setCargo("Cargo "+i);
				layoutXml.setNome("Nome "+i);
				//if(i==1)
				//layoutXml.setCpfLink("www.google.com.br");
				//else
				//	layoutXml.setCpfLink("www.facebook.com.br");
				layoutXml.setRemuneracao(UtilsModel
						.convertBigDecimalToString(new BigDecimal(element
						.getElementsByTagName("remuneracaoLiquida").item(0)
						.getTextContent())));				
				
					
				//layoutXml.setCpfCnpjCredor(getNomeCredor(
						//builder.parse(filePath.toString()
							//	+ "/CadastrosAuxiliares.xml"), cpfCnpjCredor));
				

				//Document document2 = builder.parse(filePath.toString()
					//	+ "/LancamentosContabeis.xml");

				NodeList nodeList2 = document
						.getElementsByTagName("unidadeGestora");
			//	boolean encontrouValor = false;

				// loop again if has child nodes
				// printDataNode(nodeList2);

				
				 Element element2 = (Element) nodeList2.item(0);
				 String lotacao = element2
							.getElementsByTagName("descricao")
							.item(0).getTextContent();
					layoutXml.setLotacao(lotacao);
					/*
				 * String valorPagEmpenho = element2
				 * .getElementsByTagName("lan:valorLancado").item(0)
				 * .getTextContent();
				 * 
				 * 
				 * NodeList nodeList3 =
				 * document2.getElementsByTagName("cc:EmissaoEmpenho"); for (int
				 * y = 0; y < nodeList3.getLength(); y++) { Element element3 =
				 * (Element) nodeList3.item(y); String codUnidOrc = element3
				 * .getElementsByTagName("cc:codigoUnidOrcamentaria").item(0)
				 * .getTextContent(); String numeroEmp = element3
				 * .getElementsByTagName("cc:numeroEmpenho").item(0)
				 * .getTextContent();
				 * 
				 * if(numeroEmpenho == numeroEmp &&
				 * codUnidOrcamentaria==codUnidOrc){ encontrouValor=true; break;
				 * } } if(encontrouValor){
				 * layoutXml.setValorPago(UtilsModel.convertBigDecimalToString
				 * (new BigDecimal(valorPagEmpenho))); }
				 * 
				 * }
				 */

				// NodeList childNodes = document
				// .getElementsByTagName("aux:numeroAtoQueNomeGestor");
				//
				// List<GestorXml> gestores = new ArrayList<GestorXml>();
				//
				// for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
				// Element element2 = (Element) childNodes.item(i2);
				// GestorXml gestor = new GestorXml();
				//
				// gestor.setNumero(element2
				// .getElementsByTagName("gen:numero").item(0)
				// .getTextContent());
				// gestor.setAno(element2.getElementsByTagName("gen:ano")
				// .item(0).getTextContent());
				//
				// gestores.add(gestor);
				// }
				//
				// layoutXml.setGestores(gestores);
/*
				
				layoutXmlList.add(layoutXml);
			

				setFileNameExporter("Receita_Pessoal_ano_mes");
			}
			
			for(ReceitaPessoal entidade : layoutXmlList){
				DAO dao= new DAOImpl();
				ReceitaPessoalVO create = dao.create(entidade, ReceitaPessoalVO.class);
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
		}*/
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

	public List<ReceitaPessoalVO> getLayoutXmlList() {

		return layoutXmlList;
	}

	public void setLayoutXmlList(List<ReceitaPessoalVO> layoutXmlList) {
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

	public String getNomeCPF() {
		if(nomeCPF==null)
			nomeCPF="";
		return nomeCPF;
	}

	public void setNomeCPF(String nomeCPF) {
		this.nomeCPF = nomeCPF;
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

		if (exercicio == null || exercicio == "" || getEmpresaEnum() == null)
			return new MesEnum[0];

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		List<MesEnum> meses = new ArrayList<MesEnum>();
		// ServletContext servletContext = (ServletContext) FacesContext
		// .getCurrentInstance().getExternalContext().getContext();
		// String caminho = servletContext.getRealPath(File.separator);

		String caminho = System.getProperty("user.home").concat(File.separator).concat(codmunicipio).concat(File.separator);
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

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getLinkVoltar() {
		switch (codmunicipio) {
		case "201120": {
			return "http://www.publitecportais.org/portal_transparencia/luzilandia/index-old.html";
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
