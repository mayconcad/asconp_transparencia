package br.com.asconp.publitec.controllers;

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
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.component.inputtext.InputText;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.asconp.publitec.dao.DAO;
import br.com.asconp.publitec.dao.DAOImpl;
import br.com.asconp.publitec.entities.Diaria;
import br.com.asconp.publitec.enums.EmpresaEnum;
import br.com.asconp.publitec.enums.MesEnum;
import br.com.asconp.publitec.vos.DiariaVO;

@ManagedBean(name = "DiariaController")
@ViewScoped
public class DiariaController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1087839331658006577L;

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;

	private String exercicio;

	public String nome;

	private String fileNameExporter;

	public String codmunicipio = "";

	private String linkVoltar;

	private boolean exibeMes;

	public List<Diaria> diarias;

	public InputText nomeIT;

	Calendar calDataAtual = Calendar.getInstance();

	DAO dao = null;

	@PostConstruct
	public void init() {
		calDataAtual.setTime(new Date());
		dao = new DAOImpl();
	}

	public DiariaController() {
		// TODO Auto-generated constructor stub
	}
	
	public void limpar(){
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			context.getExternalContext().redirect(
					"/publitec/pages/diaria/Diaria.xhtml?codmunicipio="+codmunicipio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void buscar() {

		if (UtilsModel.hasValue(nome) && UtilsModel.hasValue(exercicio)
				&& UtilsModel.hasValue(mesEnum) && mesEnum.ordinal() >= 0) {
			diarias = dao.find(
					Diaria.class,
					"x.codigoUnidGestora = '" + codmunicipio
							+ "' AND x.nomeCredor like '%" + getNome()
							+ "%' AND x.mesReferencia = '"
							+ String.format("%02d", getMesEnum().ordinal() + 1)
							+ "' AND x.anoReferencia = '" + getExercicio()
							+ "'");
			return;
		} else if (UtilsModel.hasValue(nome) && !UtilsModel.hasValue(exercicio)
				&& UtilsModel.hasValue(mesEnum) && mesEnum.ordinal() >= 0) {
			diarias = dao.find(
					Diaria.class,
					"x.codigoUnidGestora = '" + codmunicipio
							+ "' AND  x.nomeCredor like '%" + getNome()
							+ "%' AND x.mesReferencia = '"
							+ String.format("%02d", getMesEnum().ordinal() + 1)
							+ "'");
			return;
		} else if (UtilsModel.hasValue(nome) && UtilsModel.hasValue(exercicio)
				&& !UtilsModel.hasValue(mesEnum)) {
			diarias = dao.find(Diaria.class, "x.codigoUnidGestora = '"
					+ codmunicipio + "' AND x.nomeCredor like '%" + getNome()
					+ "%' AND x.anoReferencia = '" + getExercicio() + "'");
			return;
		} else if (UtilsModel.hasValue(nome)) {
			diarias = dao.find(Diaria.class, "x.codigoUnidGestora = '"
					+ codmunicipio + "' AND x.nomeCredor like '%" + getNome()
					+ "%'");
			return;
		} else if (UtilsModel.hasValue(mesEnum)
				&& UtilsModel.hasValue(exercicio)) {
			diarias = dao.find(
					Diaria.class,
					"x.codigoUnidGestora = '" + codmunicipio
							+ "' AND  x.mesReferencia = '"
							+ String.format("%02d", getMesEnum().ordinal() + 1)
							+ "' AND x.anoReferencia = '" + getExercicio()
							+ "'");
			if (diarias != null && !diarias.isEmpty())
				return;
		} else if (UtilsModel.hasValue(mesEnum)) {
			if (diarias != null && !diarias.isEmpty())
				diarias = dao.find(
						Diaria.class,
						"x.codigoUnidGestora = '"
								+ codmunicipio
								+ "' AND  x.mesReferencia = '"
								+ String.format("%02d",
										getMesEnum().ordinal() + 1) + "'");
			return;
		} else if (UtilsModel.hasValue(exercicio)) {
			if (diarias != null && !diarias.isEmpty())
				diarias = dao.find(Diaria.class, "x.codigoUnidGestora = '"
						+ codmunicipio + "' AND  x.anoReferencia = '"
						+ getExercicio() + "'");
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		diarias = new ArrayList<Diaria>();
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
					+ "/EmpenhoseRP.xml");

			Map<String, String> numEmpLiquidacao = new HashMap<String, String>();
			Map<String, String> numEmpPagamento = new HashMap<String, String>();

			NodeList nodeList = document.getElementsByTagName("emp:Empenho");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				Diaria diaria = new Diaria();

				String codCatEco = element
						.getElementsByTagName("emp:codigoCateEconomica")
						.item(0).getTextContent();
				String codNatDes = element
						.getElementsByTagName("emp:codigoNatuDespesa").item(0)
						.getTextContent();

				String codModApl = element
						.getElementsByTagName("emp:codigoModaAplicacao")
						.item(0).getTextContent();

				String codEleDes = element
						.getElementsByTagName("emp:codigoElemDespesa").item(0)
						.getTextContent();

				if (codCatEco == null || !"3".equals(codCatEco)
						&& codNatDes == null || !"3".equals(codNatDes)
						&& codModApl == null || !"90".equals(codModApl)
						&& codEleDes == null || !"14".equals(codEleDes))
					continue;

				diaria.setAnoReferencia(cal.get(Calendar.YEAR) + "");
				diaria.setMesReferencia(String.format("%02d",
						mesEnum.ordinal() + 1));

				String numeroEmpenho = element
						.getElementsByTagName("emp:numeroEmpenho").item(0)
						.getTextContent();

				// substituido por concatenacao de num empehno e codunidorc
				// layoutXml.setNumeroEmpenho(numeroEmpenho);
				diaria.setHistoricoEmpenho(element
						.getElementsByTagName("emp:historicoEmpenho").item(0)
						.getTextContent());

				try {
					Date date = sdf.parse(element
							.getElementsByTagName("emp:dataEmisEmpenho")
							.item(0).getTextContent());
					diaria.setDataEmisEmpenho(sdf2.format(date));

				} catch (DOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String codUnidOrcamentaria = element
						.getElementsByTagName("emp:codigoUnidOrcamentaria")
						.item(0).getTextContent();
				diaria.setCodigoUnidOrcamentaria(codUnidOrcamentaria);
				diaria.setNomeUnidOrcamentaria(getNomeUnidadeGestora(document));
				diaria.setCodigoUnidGestora(diaria.getNomeUnidOrcamentaria()
						.substring(0, 6));

				String cpfCnpjCredor = element
						.getElementsByTagName("emp:cpfCnpjCredor").item(0)
						.getTextContent();
				diaria.setCpfCnpjCredor(getNomeCredor(
						builder.parse(filePath.toString()
								+ "/CadastrosAuxiliares.xml"), cpfCnpjCredor));

				diaria.setValorEmpenho(UtilsModel
						.convertBigDecimalToString(new BigDecimal(element
								.getElementsByTagName("emp:valorEmpenho")
								.item(0).getTextContent())));

				Document document2 = builder.parse(filePath.toString()
						+ "/LancamentosContabeis.xml");

				NodeList nodeList2 = document2
						.getElementsByTagName("lan:LancamentoContabil");
				
				String chaveEmpUnidOrc = numeroEmpenho + "_"
						+ codUnidOrcamentaria;
				diaria.setNumeroEmpenho(chaveEmpUnidOrc);
				for (int x = 0; x < nodeList2.getLength(); x++) {

					String valorLancadoValido = "";
					Element element2 = (Element) nodeList2.item(x);
					String historicoRegiContabil = element2
							.getElementsByTagName("lan:historicoRegiContabil")
							.item(0).getTextContent();
					String numEmp = "0".equals(numeroEmpenho.substring(0, 1)) ? numeroEmpenho
							.substring(1, numeroEmpenho.length())
							: numeroEmpenho;
					String codUniOrc = element2
							.getElementsByTagName("lan:codigoUnidOrcamentaria")
							.item(0).getTextContent();
					String chaveEmpUnidOrcLocal = numeroEmpenho + "_"
							+ codUniOrc;

					if (!numEmpLiquidacao.containsKey(chaveEmpUnidOrcLocal))
						if (historicoRegiContabil.contains("EMP:")) {
							historicoRegiContabil = historicoRegiContabil
									.split("EMP:")[1];
							historicoRegiContabil = historicoRegiContabil
									.trim().split(" ")[0];

							if (historicoRegiContabil.equals(numEmp))
								carregarValoresLiqPagto(numEmpLiquidacao,
										numEmpPagamento, element2,
										chaveEmpUnidOrcLocal,
										valorLancadoValido);
						} else if (historicoRegiContabil.contains("EMPENHO:")) {
							historicoRegiContabil = historicoRegiContabil
									.split("EMPENHO:")[1];
							historicoRegiContabil = historicoRegiContabil
									.trim().split(" ")[0];
							if (historicoRegiContabil.equals(numEmp))
								carregarValoresLiqPagto(numEmpLiquidacao,
										numEmpPagamento, element2,
										chaveEmpUnidOrcLocal,
										valorLancadoValido);
						}
					if (!numEmpPagamento.containsKey(chaveEmpUnidOrcLocal))
						if (historicoRegiContabil.contains("EMP:")) {
							historicoRegiContabil = historicoRegiContabil
									.split("EMP:")[1];
							historicoRegiContabil = historicoRegiContabil
									.trim().split(" ")[0];

							if (historicoRegiContabil.equals(numEmp))
								carregarValoresLiqPagto(numEmpLiquidacao,
										numEmpPagamento, element2,
										chaveEmpUnidOrcLocal,
										valorLancadoValido);
						} else if (historicoRegiContabil.contains("EMPENHO:")) {
							historicoRegiContabil = historicoRegiContabil
									.split("EMPENHO:")[1];
							historicoRegiContabil = historicoRegiContabil
									.trim().split(" ")[0];
							if (historicoRegiContabil.equals(numEmp))
								carregarValoresLiqPagto(numEmpLiquidacao,
										numEmpPagamento, element2,
										chaveEmpUnidOrcLocal,
										valorLancadoValido);
						}

				}

				diarias.add(diaria);

			}

			for (Diaria despesa : diarias) {
				
				String numEmp = despesa.getNumeroEmpenho();
				if (!numEmpLiquidacao.isEmpty()
						&& numEmpLiquidacao.get(numEmp) != null)
					despesa.setValorLiquidado(UtilsModel
							.convertBigDecimalToString(new BigDecimal(
									numEmpLiquidacao.get(numEmp))));
				if (!numEmpPagamento.isEmpty()
						&& numEmpPagamento.get(numEmp) != null)
					despesa.setValorPago(UtilsModel
							.convertBigDecimalToString(new BigDecimal(
									numEmpPagamento.get(numEmp))));
				despesa.setNumeroEmpenho(despesa.getNumeroEmpenho().split("_")[0]);
				if (despesa.getCpfCnpjCredor() != null
						&& !"".equals(despesa.getCpfCnpjCredor())) {
					String[] split = despesa.getCpfCnpjCredor().split("-");
					despesa.setNomeCredor(split != null && split.length > 1 ? despesa
							.getCpfCnpjCredor().split("-")[1] : "");
				}

				List<Diaria> diarias2 = null;
				if (UtilsModel.hasValue(despesa.getCpfCnpjCredor()))
					diarias2 = dao.find(
							Diaria.class,
							" x.cpfCnpjCredor = '" + despesa.getCpfCnpjCredor()
									+ "' AND x.codigoUnidOrcamentaria = '"
									+ despesa.getCodigoUnidOrcamentaria()
									+ "' AND x.numeroEmpenho = '"
									+ despesa.getNumeroEmpenho()
									+ "' AND x.codigoUnidGestora = '"
									+ despesa.getCodigoUnidGestora()
									+ "' AND x.mesReferencia = '"
									+ despesa.getMesReferencia()
									+ "' AND x.anoReferencia = '"
									+ despesa.getAnoReferencia() + "'");
				else
					diarias2 = dao.find(
							Diaria.class,
							" x.codigoUnidOrcamentaria = '"
									+ despesa.getCodigoUnidOrcamentaria()
									+ "' AND x.numeroEmpenho = '"
									+ despesa.getNumeroEmpenho()
									+ "' AND x.codigoUnidGestora = '"
									+ despesa.getCodigoUnidGestora()
									+ "' AND x.mesReferencia = '"
									+ despesa.getMesReferencia()
									+ "' AND x.anoReferencia = '"
									+ despesa.getAnoReferencia() + "'");

				if (diarias2 != null && !diarias2.isEmpty()) {
					Diaria entidade = diarias2.get(0);
					despesa.setId(entidade.getId());
					despesa.setCpfCnpjCredor(entidade.getCpfCnpjCredor());
					despesa.setDataEmisEmpenho(entidade.getDataEmisEmpenho());
					despesa.setHistoricoEmpenho(entidade.getHistoricoEmpenho());
					despesa.setNomeCredor(entidade.getNomeCredor());
					despesa.setValorEmpenho(entidade.getValorEmpenho());
					despesa.setValorLiquidado(entidade.getValorLiquidado());
					despesa.setValorPago(entidade.getValorPago());
					dao.update(despesa, DiariaVO.class);
					// dao.closeClonection();
				} else {
					dao.create(despesa, DiariaVO.class);
					// dao.closeClonection();
				}
			}
			setFileNameExporter("Diaria_empresa_ano_mes");

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

	public void disabilitaNome(final AjaxBehaviorEvent event) {
		getNomeIT().setDisabled(false);
		if (exercicio != null && !"".equals(exercicio)) {
			getNomeIT().setDisabled(true);
			getNomeIT().setValue("");
		}

	}

	private void carregarValoresLiqPagto(Map<String, String> numEmpLiquidacao,
			Map<String, String> numEmpPagamento, Element element2,
			String chaveEmpUnidOrcLocal, String valorLancadoValido) {

		NodeList childNodes = element2.getChildNodes();

		for (int c = 0; c < childNodes.getLength(); c++) {
			Node node = childNodes.item(c);
			if (node.getChildNodes().getLength() > 1) {
				if ("lan:LancamentoContabilItem".equals(node.getNodeName())) {
					Element e = (Element) node;
					valorLancadoValido = e
							.getElementsByTagName("lan:valorLancado").item(0)
							.getTextContent();
					String tipoNatuLancamento = e
							.getElementsByTagName("lan:tipoNatuLancamento")
							.item(0).getTextContent();
					if ("2".equals(tipoNatuLancamento)) {
						Node childNode2 = getChildNodeByNome(
								node.getChildNodes(), "lan:ContaCorrente");
						if (childNode2 != null) {
							Node childNode3 = getChildNodeByNome(
									childNode2.getChildNodes(),
									"cc:LiquidacaoEmpenho");
							if (childNode3 != null) {
								Element e3 = (Element) childNode3;
								// String codigoUnidOrc = e3
								// .getElementsByTagName(
								// "cc:codigoUnidOrcamentaria")
								// .item(0).getTextContent();
								String codigoContContabil = e3
										.getElementsByTagName(
												"cc:codigoContContabil")
										.item(0).getTextContent();

								if ("62213".equals(codigoContContabil
										.substring(0, 5))) {
									String codigoEmpUnidOrc = chaveEmpUnidOrcLocal;
									// if("0044001_020901".equals(codigoEmpUnidOrc))
									// System.out.println("teste");
									if (numEmpLiquidacao
											.containsKey(codigoEmpUnidOrc)) {
										String valorLiq = numEmpLiquidacao
												.get(codigoEmpUnidOrc);
										valorLiq = new BigDecimal(valorLiq)
												.add(new BigDecimal(
														valorLancadoValido))
												.toString();
										numEmpLiquidacao
												.remove(codigoEmpUnidOrc);
										numEmpLiquidacao.put(codigoEmpUnidOrc,
												valorLiq);
									} else {
										numEmpLiquidacao.put(codigoEmpUnidOrc,
												valorLancadoValido);

									}
								}
							}

							Node childNode4 = getChildNodeByNome(
									childNode2.getChildNodes(),
									"cc:DomicilioBancario");
							if (childNode4 != null) {
								Element e4 = (Element) childNode4;
								// String codigoUnidOrc = e4
								// .getElementsByTagName(
								// "cc:codigoUnidOrcamentaria")
								// .item(0).getTextContent();
								String codigoContContabil = e4
										.getElementsByTagName(
												"cc:codigoContContabil")
										.item(0).getTextContent();

								if ("11111".equals(codigoContContabil
										.substring(0, 5))) {
									String codigoEmpUnidOrc = chaveEmpUnidOrcLocal;
									if (numEmpPagamento
											.containsKey(codigoEmpUnidOrc)) {
										String valorLiq = numEmpPagamento
												.get(codigoEmpUnidOrc);
										valorLiq = new BigDecimal(valorLiq)
												.add(new BigDecimal(
														valorLancadoValido))
												.toString();
										numEmpPagamento
												.remove(codigoEmpUnidOrc);
										numEmpPagamento.put(codigoEmpUnidOrc,
												valorLiq);

									} else {
										numEmpPagamento.put(codigoEmpUnidOrc,
												valorLancadoValido);
									}
								}
							}
						}

					}

				}
			}
		}

		/*
		 * Node childNode = getChildNodeByNome(childNodes,
		 * "lan:LancamentoContabilItem"); if (childNode != null) { Element e =
		 * (Element) childNode; valorLancadoValido = e
		 * .getElementsByTagName("lan:valorLancado") .item(0).getTextContent();
		 * String tipoNatuLancamento = e
		 * .getElementsByTagName("lan:tipoNatuLancamento")
		 * .item(0).getTextContent(); if("2".equals(tipoNatuLancamento)){ Node
		 * childNode2 = getChildNodeByNome( childNode.getChildNodes(),
		 * "lan:ContaCorrente"); if (childNode2 != null) { Node childNode3 =
		 * getChildNodeByNome( childNode2.getChildNodes(),
		 * "cc:LiquidacaoEmpenho"); if (childNode3 != null) { Element e3 =
		 * (Element) childNode3; String codigoContContabil = e3
		 * .getElementsByTagName( "cc:codigoContContabil")
		 * .item(0).getTextContent();
		 * 
		 * if ("62213".equals(codigoContContabil .substring(0, 5))) { if
		 * (numEmpLiquidacao .containsKey(numeroEmpenho)){ String valorLiq =
		 * numEmpLiquidacao.get(numeroEmpenho); valorLiq= new
		 * BigDecimal(valorLiq).add(new
		 * BigDecimal(valorLancadoValido)).toString();
		 * numEmpLiquidacao.remove(numeroEmpenho);
		 * numEmpLiquidacao.put(numeroEmpenho, valorLiq); }else{
		 * numEmpLiquidacao.put(numeroEmpenho, valorLancadoValido);
		 * 
		 * } } }
		 * 
		 * } Node childNode4 = getChildNodeByNome( childNode.getChildNodes(),
		 * "cc:DomicilioBancario"); if (childNode4 != null) { Element e4 =
		 * (Element) childNode4; String codigoContContabil = e4
		 * .getElementsByTagName( "cc:codigoContContabil")
		 * .item(0).getTextContent();
		 * 
		 * if ("11111".equals(codigoContContabil .substring(0, 5))) { if
		 * (numEmpPagamento .containsKey(numeroEmpenho)){ String valorLiq =
		 * numEmpLiquidacao.get(numeroEmpenho); valorLiq = new
		 * BigDecimal(valorLiq).add(new
		 * BigDecimal(valorLancadoValido)).toString();
		 * numEmpLiquidacao.remove(numeroEmpenho);
		 * numEmpLiquidacao.put(numeroEmpenho, valorLiq);
		 * 
		 * }else{ numEmpPagamento.put( numeroEmpenho, valorLancadoValido); } } }
		 * } }
		 */
	}

	private Node getChildNodeByNome(NodeList childNodes, String nome) {
		for (int c = 0; c < childNodes.getLength(); c++) {
			Node item = childNodes.item(c);
			String nodeName = item.getNodeName();
			if (nome.equals(nodeName)) {
				return item;
			}
		}
		return null;
	}

	private String obterValorLancado(String codUnidOrcamentaria, List elementos) {

		String result = null;
		if (elementos.size() > 0) {

			Iterator it = elementos.iterator();

			while (it.hasNext()) {
				org.jdom2.Element item = (org.jdom2.Element) it.next();
				if ("LancamentoContabil".equals(item.getName())) {

					return item.getChild("LancamentoContabilItem")
							.getChildText("valorLancado");

					//
					// if (child != null) {
					// result = child;
					// } else {
					//
					// result = obterValorLancado(codUnidOrcamentaria,
					// item.getChildren());
					// if (result == null)
					// continue;
					// }
				} else {
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

	public List<Diaria> getDiarias() {

		return diarias;
	}

	public void setDiarias(List<Diaria> diarias) {
		this.diarias = diarias;
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

		String caminho = System.getProperty("user.home").concat(File.separator)
				.concat(codmunicipio).concat(File.separator);

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
			return EmpresaEnum.CM_SAO_FELIX.toString();
		}
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

	public InputText getNomeIT() {
		return nomeIT;
	}

	public void setNomeIT(InputText nomeIT) {
		this.nomeIT = nomeIT;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
