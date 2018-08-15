package br.com.asconp.publitec.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
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
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.asconp.publitec.entities.DemonstrativoReceita;
import br.com.asconp.publitec.enums.EmpresaEnum;
import br.com.asconp.publitec.enums.MesEnum;

@ManagedBean(name = "DemonstrativoReceitaController")
@ViewScoped
public class DemonstrativoReceitaController extends BaseController {

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;

	private String exercicio;

	private String fileNameExporter;

	public String codmunicipio = "";
	
	private String linkVoltar;

	// private ExcelOptions excelOpt;
	//
	// private PDFOptions pdfOpt;

	private boolean exibeMes;

	private List<DemonstrativoReceita> receitas;

	Calendar calDataAtual = Calendar.getInstance();

	@PostConstruct
	public void init() {
		calDataAtual.setTime(new Date());
	}
	
	public void limpar(){
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			context.getExternalContext().redirect(
					"/publitec/pages/receita/Receita.xhtml?codmunicipio="+codmunicipio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public List<DemonstrativoReceita> getReceitas() {
		return receitas;
	}

	public void setReceitas(List<DemonstrativoReceita> receitas) {
		this.receitas = receitas;
	}

	public void buscar() {

		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String caminho2 = servletContext.getRealPath(File.separator
				+ "TabelasInternas.xml");

		DocumentBuilder builder2;
		Map<String, String> codDescricaoMap = new HashMap<String, String>();

		try {
			builder2 = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document document2 = builder2.parse(caminho2.toString());

			NodeList nodeList2 = document2
					.getElementsByTagName("xs:restriction");

			// Node namedItem2 =
			// nodeList2.item(6).getAttributes().getNamedItem("name");
			// NodeList nodeCodRecOrc = nodeList2.item(6).getChildNodes();

			// for (int i = 0; i < nodeList2.getLength(); i++) {
			Element element2 = (Element) nodeList2.item(0);

			NodeList valores = element2.getElementsByTagName("xs:enumeration");

			for (int x = 0; x < valores.getLength(); x++) {

				Node item = valores.item(x);
				NamedNodeMap attributes = item.getAttributes();
				Node namedItem = attributes.getNamedItem("value");
				String especificacao = namedItem.toString().split("=")[1];
				String[] codDesc = especificacao.split(" - ");
				String codigo = codDesc[0].replace('"', ' ');
				String descricao = codDesc[2].replace('"', ' ');
				descricao += " ";
				descricao += codDesc.length > 3 ? codDesc[3].replace('"', ' ')
						: "";

				if (!codDescricaoMap.containsKey(codigo.trim())) {
					codDescricaoMap.put(codigo.trim(), descricao);
				}
				// }
			}
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * ESSE CODIGO DEVE SERVIR APENAS PARA O MES DE JANEIRO
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		receitas = new ArrayList<DemonstrativoReceita>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			String filePathAnterior="";
			String caminho = System.getProperty("user.home").concat(
					File.separator);

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(empresaEnum.getCodigo())
					.append(File.separator).append(cal.get(Calendar.YEAR))
					.append(File.separator);
			filePathAnterior=filePath.toString()+String.format("%02d", mesEnum.ordinal() ).concat(File.separator);
			filePath.append(String.format("%02d", mesEnum.ordinal() + 1))
					.append(File.separator);

			Document document = builder.parse(filePath.toString()
					+ "/LancamentosContabeis.xml");

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
			 * 
			 * 
			 * //NodeList childNodes = element.getChildNodes();
			 * 
			 * layoutXmlList.add( layoutXml ); }
			 */

			NodeList nodeList = document
					.getElementsByTagName("lan:LancamentoContabil");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

			Map<String, BigDecimal> codRecOrcValLancadoPrevisaoMap = new HashMap<String, BigDecimal>();

			Map<String, BigDecimal> codRecOrcValLancadoMesMap = new HashMap<String, BigDecimal>();

			if (mesEnum.ordinal() + 1 > 1) {
				processarDemonstrativoDemaisMeses(filePathAnterior, codDescricaoMap, nodeList,
						
						codRecOrcValLancadoMesMap);
				if(receitas.isEmpty())
					processarDemonstrativoJaneiro(codDescricaoMap, nodeList,
							codRecOrcValLancadoPrevisaoMap,
							codRecOrcValLancadoMesMap);
			} else {
				processarDemonstrativoJaneiro(codDescricaoMap, nodeList,
						codRecOrcValLancadoPrevisaoMap,
						codRecOrcValLancadoMesMap);
			}

			receitas.sort(new Comparator<DemonstrativoReceita>() {

				@Override
				public int compare(DemonstrativoReceita o1,
						DemonstrativoReceita o2) {
					return o1.getCodigo().compareTo(o2.getCodigo());
				}
			});

			criarArquivoArrecadacaoMes(mesEnum.ordinal() + 1,
					filePath.toString(), receitas);

			// setFileNameExporter("Despesa_empresa_ano_mes");

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

		setFileNameExporter("Demonstrativo_Receitas_" + getExercicio() + "_"
				+ getMesEnum().toString());
	}

	private void processarDemonstrativoJaneiro(
			Map<String, String> codDescricaoMap, NodeList nodeList,
			Map<String, BigDecimal> codRecOrcValLancadoPrevisaoMap,
			Map<String, BigDecimal> codRecOrcValLancadoMesMap) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String tipoNatuLancamento = element
					.getElementsByTagName("lan:tipoNatuLancamento").item(0)
					.getTextContent();

			String valorLancado = element
					.getElementsByTagName("lan:valorLancado").item(0)
					.getTextContent();

			/*
			 * Node previsaoReceitaOrcamentariaNO = element
			 * .getElementsByTagName("cc:ReceitaArrecadar").item(0);
			 * 
			 * 
			 * if(previsaoReceitaOrcamentariaNO == null) continue;
			 */

			Node elementsByTagName = element.getElementsByTagName(
					"cc:codigoReceOrcamentaria").item(0);
			if (elementsByTagName == null)
				continue;
			String codRecOrcamentaria = elementsByTagName.getTextContent();
			codRecOrcamentaria = codRecOrcamentaria.trim();

			if ("1".equals(tipoNatuLancamento)) {
				if (codRecOrcValLancadoMesMap.containsKey(codRecOrcamentaria)) {
					BigDecimal bigDecimal = codRecOrcValLancadoMesMap
							.get(codRecOrcamentaria);
					bigDecimal = bigDecimal.add(BigDecimal.valueOf(Double
							.valueOf(valorLancado)));

					codRecOrcValLancadoMesMap.remove(codRecOrcamentaria);
					codRecOrcValLancadoMesMap.put(codRecOrcamentaria,
							bigDecimal);
				} else {
					codRecOrcValLancadoMesMap.put(codRecOrcamentaria,
							BigDecimal.valueOf(Double.valueOf(valorLancado)));
				}
			}
		}

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String valorLancado = element
					.getElementsByTagName("lan:valorLancado").item(0)
					.getTextContent();

			Node previsaoReceitaOrcamentariaNO = element.getElementsByTagName(
					"cc:PrevisaoReceitaOrcamentaria").item(0);

			if (previsaoReceitaOrcamentariaNO == null)
				continue;

			String codRecOrcamentaria = element
					.getElementsByTagName("cc:codigoReceOrcamentaria").item(0)
					.getTextContent();
			codRecOrcamentaria = codRecOrcamentaria.trim();

			if (!codRecOrcValLancadoPrevisaoMap.containsKey(codRecOrcamentaria)) {
				codRecOrcValLancadoPrevisaoMap.put(codRecOrcamentaria,
						BigDecimal.valueOf(Double.valueOf(valorLancado)));
			}

		}

		for (String key : codRecOrcValLancadoPrevisaoMap.keySet()) {
			DemonstrativoReceita dr = new DemonstrativoReceita();
			BigDecimal valorLancamento=codRecOrcValLancadoPrevisaoMap
					.get(key);
			dr.setReceitaPrevista(UtilsModel
					.convertBigDecimalToString(valorLancamento));
			if (codRecOrcValLancadoMesMap.containsKey(key)){
				BigDecimal valorMes=codRecOrcValLancadoMesMap
						.get(key);
				valorMes = valorMes.subtract(valorLancamento);
				dr.setArrecadacaoMes(UtilsModel
						.convertBigDecimalToString(valorMes));
				dr.setArrecadacaoAcumulada(UtilsModel
						.convertBigDecimalToString(valorMes));
			}
			if (key.equals("17210104"))
				dr.setTitulo(codDescricaoMap.get("17210102"));
			else
				dr.setTitulo(codDescricaoMap.get(key));
			dr.setCodigo(key);

			receitas.add(dr);
		}
	}

	private void processarDemonstrativoDemaisMeses(String filePathName,
			Map<String, String> codDescricaoMap, NodeList nodeList,			
			Map<String, BigDecimal> codRecOrcValLancadoMesMap) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String tipoNatuLancamento = element
					.getElementsByTagName("lan:tipoNatuLancamento").item(0)
					.getTextContent();

			String valorLancado = element
					.getElementsByTagName("lan:valorLancado").item(0)
					.getTextContent();

			/*
			 * Node previsaoReceitaOrcamentariaNO = element
			 * .getElementsByTagName("cc:ReceitaArrecadar").item(0);
			 * 
			 * 
			 * if(previsaoReceitaOrcamentariaNO == null) continue;
			 */

			Node elementsByTagName = element.getElementsByTagName(
					"cc:codigoReceOrcamentaria").item(0);
			if (elementsByTagName == null)
				continue;
			
			String codRecOrcamentaria = elementsByTagName.getTextContent();
			codRecOrcamentaria = codRecOrcamentaria.trim();

			if ("1".equals(tipoNatuLancamento)) {
				if (codRecOrcValLancadoMesMap.containsKey(codRecOrcamentaria)) {
					BigDecimal bigDecimal = codRecOrcValLancadoMesMap
							.get(codRecOrcamentaria);
					bigDecimal = bigDecimal.add(BigDecimal.valueOf(Double
							.valueOf(valorLancado)));

					codRecOrcValLancadoMesMap.remove(codRecOrcamentaria);
					codRecOrcValLancadoMesMap.put(codRecOrcamentaria,
							bigDecimal);
				} else {
					codRecOrcValLancadoMesMap.put(codRecOrcamentaria,
							BigDecimal.valueOf(Double.valueOf(valorLancado)));
				}
			}
		}

		/*for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String valorLancado = element
					.getElementsByTagName("lan:valorLancado").item(0)
					.getTextContent();

			Node previsaoReceitaOrcamentariaNO = element.getElementsByTagName(
					"cc:PrevisaoReceitaOrcamentaria").item(0);

			if (previsaoReceitaOrcamentariaNO == null)
				continue;

			String codRecOrcamentaria = element
					.getElementsByTagName("cc:codigoReceOrcamentaria").item(0)
					.getTextContent();
			codRecOrcamentaria = codRecOrcamentaria.trim();

			if (!codRecOrcValLancadoPrevisaoMap.containsKey(codRecOrcamentaria)) {
				codRecOrcValLancadoPrevisaoMap.put(codRecOrcamentaria,
						BigDecimal.valueOf(Double.valueOf(valorLancado)));
			}

		}*/
		
		Map<String, String> valoresDemonstrativo = getValorDemonstrativo(filePathName);

		for (String key : valoresDemonstrativo.keySet()) {
			
			DemonstrativoReceita dr = new DemonstrativoReceita();
//			dr.setReceitaPrevista(UtilsModel
//					.convertBigDecimalToString(BigDecimal.valueOf(Double.valueOf(valoresDemonstrativo
//							.get(key).split("-")[0]))));
			dr.setReceitaPrevista(valoresDemonstrativo
							.get(key).split("-")[0]);
			String key2 = key.replace(".", "");
			if (codRecOrcValLancadoMesMap.containsKey(key2)){
				BigDecimal valor = codRecOrcValLancadoMesMap
						.get(key2); 
				dr.setArrecadacaoMes(UtilsModel
						.convertBigDecimalToString(valor));
				String valorMapa = valoresDemonstrativo
				.get(key).split("-")[1];
				valorMapa=valorMapa.replace("R$", "").replace(".", "").replace(",", ".").trim();
				valor = valor.add(BigDecimal.valueOf(Double.valueOf(valorMapa)));
				dr.setArrecadacaoAcumulada(UtilsModel
						.convertBigDecimalToString(valor));
			}else{
				
				dr.setArrecadacaoMes(UtilsModel
						.convertBigDecimalToString(BigDecimal.ZERO));
				String valorMapa = valoresDemonstrativo
				.get(key).split("-")[1];
				valorMapa=valorMapa.replace("R$", "").replace(".", "").replace(",", ".").trim();
				//valor = valor.add(BigDecimal.valueOf(Double.valueOf(valorMapa)));
				dr.setArrecadacaoAcumulada(UtilsModel
						.convertBigDecimalToString(BigDecimal.valueOf(Double.valueOf(valorMapa))));
				
			}
			if (key2.equals("17210104"))
				dr.setTitulo(codDescricaoMap.get("17210102"));
			else
				dr.setTitulo(codDescricaoMap.get(key2));
			dr.setCodigo(key2);

			receitas.add(dr);
		}
	}

	private void criarArquivoArrecadacaoMes(int numeroMes, String filePath,
			List<DemonstrativoReceita> list) {

		HSSFWorkbook workbook = new HSSFWorkbook();

		String filename = "arecadacao.xls";
		HSSFSheet firstSheet = workbook.createSheet("Arrecadacao-" + numeroMes);

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(filePath + filename));

			int i = 0;

			for (DemonstrativoReceita dr : list) {
				HSSFRow row = firstSheet.createRow(i);
				if (i > 0) {
					row.createCell(0).setCellValue(dr.getCodigo());
					row.createCell(1).setCellValue(dr.getReceitaPrevista());
					row.createCell(2).setCellValue(dr.getArrecadacaoAcumulada());
				} else {
					row.createCell(0).setCellValue("CODIGO");
					row.createCell(1).setCellValue("VALOR_PREVISAO");
					row.createCell(2).setCellValue("VALOR_MES");
				}
				// row.createCell(2).setCellValue(cd.valor);
				// row.createCell(3).setCellValue(cd.getPreco());
				// row.createCell(4).setCellValue(cd.getAutor());
				// row.createCell(5).setCellValue(cd.getMusicas());
				// row.createCell(6).setCellValue(cd.getGravadora());
				// row.createCell(7).setCellValue(cd.getFoto());

				i++;

			} // fim do for

			workbook.write(fos);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao exportar arquivo");
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private Map<String, String> getValorDemonstrativo(String filePathName) {
		
		Map<String, String> demonstrativoMap=new HashMap<String, String>();
		FileInputStream stream = null;

		filePathName = filePathName+"arecadacao.xls";
		File file = new File(filePathName);
		try {
			stream = new FileInputStream(file);

			//XSSFWorkbook workBook = new XSSFWorkbook(file);
			Workbook workBook = WorkbookFactory.create(stream);		
				

			//XSSFSheet sheet = workBook.getSheetAt(0);
			Sheet sheet = workBook.getSheetAt(0);

			Iterator<Row> rows = sheet.iterator();

			while (rows.hasNext()) {
				Row row = rows.next();

				Iterator<Cell> cells = row.iterator();
int i=0;
String codigo="";
				while (cells.hasNext()) {

					Cell cell = cells.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if(Arrays.asList(new String[]{"CODIGO","VALOR_PREVISAO","VALOR_MES"}).contains(cell.getStringCellValue()))
						break;
						
						if(i==0){
							codigo=cell.getStringCellValue();
							demonstrativoMap.put(codigo, "");
						}else if(i==1){
							String valor = demonstrativoMap.get(codigo);
							valor=valor+cell.getStringCellValue();
							demonstrativoMap.remove(codigo);
							demonstrativoMap.put(codigo,valor);
						}else if(i==2){
							String valor = demonstrativoMap.get(codigo);
							valor=valor+"-"+cell.getStringCellValue();
							demonstrativoMap.remove(codigo);
							demonstrativoMap.put(codigo,valor);
						}
						
						i++;
						break;
					/*case Cell.CELL_TYPE_NUMERIC:
						System.out.println("TIPO NUMÉRICO: "
								+ cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						System.out.println("TIPO FÓRMULA: "
								+ cell.getCellFormula());
						break;*/
					}
				}
			}

		} catch (FileNotFoundException e) {
			java.util.logging.Logger.getLogger(
					DemonstrativoReceitaController.class.getName()).log(
					java.util.logging.Level.SEVERE, null, e);
			e.printStackTrace();
		} catch (IOException e) {
			java.util.logging.Logger.getLogger(
					DemonstrativoReceitaController.class.getName()).log(
					java.util.logging.Level.SEVERE, null, e);
			e.printStackTrace();
		}catch(InvalidFormatException e){
			java.util.logging.Logger.getLogger(
					DemonstrativoReceitaController.class.getName()).log(
					java.util.logging.Level.SEVERE, null, e);
			e.printStackTrace();
		}
		return demonstrativoMap;
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

	
	// @Override
	// public void onTabChanged(TabChangeEvent event) {
	// // TODO Auto-generated method stub
	//
	// }

}
