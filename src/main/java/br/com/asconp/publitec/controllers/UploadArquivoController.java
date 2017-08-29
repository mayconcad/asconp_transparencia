package br.com.asconp.publitec.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.internal.core.ModelUpdater;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



















import br.com.asconp.publitec.dao.DAO;
import br.com.asconp.publitec.dao.DAOImpl;
import br.com.asconp.publitec.entities.LayoutXml;
import br.com.asconp.publitec.entities.ReceitaPessoal;
import br.com.asconp.publitec.enums.EmpresaEnum;
import br.com.asconp.publitec.enums.MesEnum;
import br.com.asconp.publitec.vos.ReceitaPessoalVO;

@ManagedBean(name = "UploadArquivoController")
@ViewScoped
public class UploadArquivoController extends BaseController {

	private UploadedFile arquivoUploadFile;

	private InputText arquivoNomeFileUpload;

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;
	
	private String exercicio;
	
	private String tipoArquivo="ReceitaDespesa";

	public List<LayoutXml> layoutXmlList;

	private String login, password;
	
	Calendar calDataAtual=Calendar.getInstance();
	
	Boolean [] listFiles={false,false,false};

	@PostConstruct
	public void init() {
		calDataAtual.setTime(new Date());
		empresaEnum = null;
		mesEnum = null;
		
	}

	public UploadArquivoController() {
		LoginBean controllerInstance = UtilsView
				.getControllerInstance(LoginBean.class);
		if (/*controllerInstance.getUserName() == null
				|| controllerInstance.getPassword() == null
			|| !controllerInstance.getUserName().trim().equals("asconp") || !controllerInstance.getPassword().trim().equals("asconp2017")*/ false) {
		
			FacesContext context = FacesContext.getCurrentInstance();
			try {
				context.getExternalContext().redirect(
						"/publitec/login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setLogin(controllerInstance.getUserName());
		setPassword(controllerInstance.getPassword());
	}

	public void handleFileUpload(FileUploadEvent event) {
		if (!UtilsModel.possuiValorValido(empresaEnum, mesEnum)) {
			FacesMessage message = new FacesMessage(
					"Informe os campos Empresa e Mês");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		} else if (!UtilsModel.possuiValorValido(empresaEnum)) {
			FacesMessage message = new FacesMessage("Informe a Empresa");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		} else if (!UtilsModel.possuiValorValido(mesEnum)) {
			FacesMessage message = new FacesMessage("Informe o Mês");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		if (salvarArquivo(event.getFile())) {

			// FacesMessage message = new
			// FacesMessage(event.getFile().getFileName() + " foi importado.");
			// FacesContext.getCurrentInstance().addMessage(null, message);
			if (FacesContext.getCurrentInstance().getMessageList().size() == 0) {
				FacesMessage message = new FacesMessage(
						"Arquivo(s) importado(s) com sucesso!");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}else{
			FacesMessage message = new FacesMessage(
					"Não fo possível realizar a importação do(s) arquivo(s)!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		String fileName=event.getFile().getFileName();
		
		
			if("FolhaPagamento.xml".equals(fileName)){
				listFiles[0]=true;
			}
			if("Servidor.xml".equals(fileName) ){
				listFiles[1]=true;
			}
			if("Cargo.xml".equals(fileName) ){
				listFiles[2]=true;
			}		
				
		
		if(listFiles[0] && listFiles[1] && listFiles[2])
			if(UtilsModel.hasValue(getTipoArquivo()) && "Pessoal".equals(getTipoArquivo())){
				processarArquivoPessoal();
				listFiles[0] = false;
				listFiles[1] = false;
				listFiles[2] = false;
			}
		
		
		init();
		return;
	}
	
	public void processarArquivoPessoal() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		ArrayList<ReceitaPessoal> receitasPessoal = new ArrayList<ReceitaPessoal>();
		try {
			DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
			newInstance.setNamespaceAware(true);


			String caminho = System.getProperty("user.home").concat(
					File.separator);

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(empresaEnum.getCodigo())
					.append(File.separator).append(cal.get(Calendar.YEAR))
					.append(File.separator);

			filePath.append(String.format("%02d", MesEnum.JAN.ordinal() + 1))
					.append(File.separator);
			
			File file0 = new File(filePath.toString()
					+ "FolhaPagamento.xml");
			InputStream inputStream0= new FileInputStream(file0);
			Reader reader0 = new InputStreamReader(inputStream0,"UTF-8");
			
			InputSource is0 = new InputSource(reader0);
			is0.setEncoding("UTF-8");
			
			DocumentBuilder builder = newInstance
					.newDocumentBuilder();

			Document document = builder.parse(is0);		

			NodeList nodeList = document.getElementsByTagName("FolhaPagamento");
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				ReceitaPessoal layoutXml = new ReceitaPessoal();

				String cpf = element
						.getElementsByTagName("cpf").item(0)
						.getTextContent();
				
				String numeroCargo=element
						.getElementsByTagName("numeroCargo").item(0)
						.getTextContent();
				
				String mesReferencia=element
						.getElementsByTagName("mesReferencia").item(0)
						.getTextContent();
				if(UtilsModel.hasValue(mesReferencia))
					layoutXml.setMes( mesReferencia);
				layoutXml.setCpf(cpf);
				layoutXml.setAno(cal.get(Calendar.YEAR));
				layoutXml.setNumunidadegestora(empresaEnum.getCodigo());
				
				
				File file = new File(filePath.toString()+ "/Cargo.xml");
				InputStream inputStream= new FileInputStream(file);
				Reader reader = new InputStreamReader(inputStream,"UTF-8");

				InputSource is = new InputSource(reader);
				
				File file2 = new File(filePath.toString()+ "/Servidor.xml");
				InputStream inputStream2= new FileInputStream(file2);
				Reader reader2 = new InputStreamReader(inputStream2,"UTF-8");

				InputSource is2 = new InputSource(reader2);

				
//				SAXBuilder saxBuilder = new SAXBuilder();
//				org.jdom2.Document doc;
//				try {
//					doc = saxBuilder.build(is);
//
//				org.jdom2.Element rootElement = (org.jdom2.Element) doc
//						.getRootElement();
//				for (org.jdom2.Element elementList : rootElement.getChildren()) {				   
//				   
//					for(org.jdom2.Element elementCargo : elementList.getChildren() ){
//				    if("numero".equals(elementCargo.getName())){
//				    	if(numeroCargo.equals(elementCargo.getValue()))
//				    		System.out.println(elementCargo.getValue());
//				    }
//					}
//				}
//				
//				} catch (JDOMException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				layoutXml.setCargo("Cargo - ");
//				layoutXml.setNome("Nome - ");
				layoutXml.setCargo(getCargoFuncionario(builder.parse(is),numeroCargo));
				layoutXml.setNome(getNomeFuncionario(builder.parse(is2),cpf));
				
				layoutXml.setRemuneracao(UtilsModel
						.convertBigDecimalToString(new BigDecimal(element
						.getElementsByTagName("remuneracaoLiquida").item(0)
						.getTextContent())));	
				
				
				NodeList nodeList2 = document
						.getElementsByTagName("unidadeGestora");
			
				 Element element2 = (Element) nodeList2.item(0);
				 String lotacao = element2
							.getElementsByTagName("descricao")
							.item(0).getTextContent();
					layoutXml.setLotacao(lotacao);
								
					receitasPessoal.add(layoutXml);			

				
			}
			
			DAO dao= new DAOImpl();
			for(ReceitaPessoal entidade : receitasPessoal){
				Map<String,Object> params=new HashMap<String, Object>();
				params.put("nome", entidade.getNome().trim());
				params.put("cpf", entidade.getCpf().trim());
				params.put("numunidadegestora", entidade.getNumunidadegestora().trim());
				List<ReceitaPessoalVO> find = dao.find(ReceitaPessoal.class, params,ReceitaPessoalVO.class ,false);
				if(find == null || find.isEmpty())
				dao.create(entidade, ReceitaPessoalVO.class);
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
	private String getCargoFuncionario(Document document, String numeroCargo) {

		String result = "";
		NodeList nodeList = document.getElementsByTagName("Cargo");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String cpfCnpjCred = element
					.getElementsByTagName("numero").item(0)
					.getTextContent();
			if (cpfCnpjCred.equals(numeroCargo)) {
				result = element
						.getElementsByTagName("nome")
						.item(0).getTextContent();
				break;
			}
		}

		return result;
	}
	
	private String getNomeFuncionario(Document document, String cpf) {

		String result = "";
		NodeList nodeList = document.getElementsByTagName("Servidor");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);

			String cpfCnpjCred = element
					.getElementsByTagName("cpf").item(0)
					.getTextContent();
			if (cpfCnpjCred.equals(cpf)) {
				result = element
						.getElementsByTagName("nome")
						.item(0).getTextContent();
				break;
			}
		}

		return result;
	}

	private boolean salvarArquivo(UploadedFile uploadedFile) {

		String numeroMes = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, Integer.parseInt(exercicio));
		try {
			//Caminho do servidor
//			ServletContext servletContext = (ServletContext) FacesContext
//					.getCurrentInstance().getExternalContext().getContext();
//			String caminho = servletContext.getRealPath(File.separator);
			String caminho = System.getProperty("user.home").concat(File.separator);

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(getEmpresaEnum().getCodigo())
					.append(File.separator);

			if (!Paths.get(filePath.toString()).toFile().exists()) {
				new File(filePath.toString()).mkdir();
			}
			filePath.append(cal.get(Calendar.YEAR)).append(File.separator);			
			// StringUtils.leftPad(mesEnum.ordinal()+1, 2, "0")
			if (!Paths.get(filePath.toString()).toFile().exists()) {
				new File(filePath.toString()).mkdir();
			}
			numeroMes = String.format("%02d", mesEnum.ordinal() + 1);
			filePath.append(numeroMes).append(File.separator);
			if (!Paths.get(filePath.toString()).toFile().exists()) {
				new File(filePath.toString()).mkdir();
			}

			File file = new File(filePath.append(uploadedFile.getFileName())
					.toString());

			FileOutputStream in = new FileOutputStream(file);
			in.write(uploadedFile.getContents());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			addErrorMessage("Ocorreu um erro ao salvar o arquivo!");
			return false;
		}
		if(UtilsModel.hasValue(getTipoArquivo()) && !"Pessoal".equals(getTipoArquivo())){
		// verificar o conteudo do arquivo se a empresa selecionada corresponde
		// a empresa do arquivo
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
//caminho do servidor
//			ServletContext servletContext = (ServletContext) FacesContext
//					.getCurrentInstance().getExternalContext().getContext();
//			String caminho = servletContext.getRealPath(File.separator);
			String caminho = System.getProperty("user.home").concat(File.separator);
			
			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(getEmpresaEnum().getCodigo())
					.append(File.separator).append(cal.get(Calendar.YEAR))
					.append(File.separator);

			filePath.append(numeroMes).append(File.separator);

			Document document = builder.parse(filePath.toString()
					+ "/EmpenhoseRP.xml");

			NodeList nodeList = document
					.getElementsByTagName("emp:PrestacaoContas");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);

				String codUnidGestora = element
						.getElementsByTagName("aux:codigoUnidGestora").item(0)
						.getTextContent();
				String anoRef = element
						.getElementsByTagName("aux:anoReferencia").item(0)
						.getTextContent();
				String mesRef = element
						.getElementsByTagName("aux:mesReferencia").item(0)
						.getTextContent();
				String urlDir = empresaEnum.getCodigo() + "" + File.separator
						+ cal.get(Calendar.YEAR) + File.separator + numeroMes
						+ File.separator;
				if (!exercicio.equals(anoRef)) {
					Paths.get(caminho + urlDir).toFile().delete();
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Arquivo(s) NÃO importado(s) (O ano de referência informado não corresponde ao do(s) arquivo(s))!",
							"");
					FacesContext.getCurrentInstance().addMessage(null, message);
					break;
				}
				if (!empresaEnum.getCodigo().equals(codUnidGestora)) {
					Paths.get(caminho + urlDir).toFile().delete();
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Arquivo(s) NÃO importado(s) (A unidade gestora informada não corresponde a do(s) arquivo(s))!",
							"");
					FacesContext.getCurrentInstance().addMessage(null, message);
					break;
				}
				if (!numeroMes.equals(String.format("%02d",
						Integer.parseInt(mesRef)))) {
					Paths.get(caminho + urlDir).toFile().delete();
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Arquivo(s) NÃO importado(s) (O mês de referência informado não corresponde ao do(s) arquivo(s))!",
							"");
					FacesContext.getCurrentInstance().addMessage(null, message);
					break;
				}
			}
		} catch (ParserConfigurationException e) {
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

		return true;
	}

	public String[] getExercicios(){
		List<String> exercicios=new ArrayList<String>();
		
		for(int i = 2012; i <= calDataAtual.get(Calendar.YEAR); i++){
			exercicios.add(i+"");
		}
		String[] exers=new String[exercicios.size()];
		int x = 0;
		for (String ano : exercicios) {
			exers[x] = ano;
			x++;
		}
		
		return exers;
	}
	
	
	

	public List<LayoutXml> getLayoutXmlList() {
		layoutXmlList = new ArrayList<LayoutXml>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			String property = System.getProperty("user.home");
			String dir = System.getProperty("wtp.deploy")
					+ "/publitec";
			// URL url = new URL( dir+"/CadastrosAuxiliares.xml" );
			Document document = builder.parse(dir + "/CadastrosAuxiliares.xml");

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

			// NodeList nodeList = document.getElementsByTagName( "aux:Gestor"
			// );
			//
			// for ( int i = 0; i < nodeList.getLength(); i++ ) {
			// Element element = (Element) nodeList.item( i );
			// LayoutXml layoutXml = new LayoutXml();
			//
			// layoutXml.setCpfAgenPolitico( element.getElementsByTagName(
			// "aux:cpfAgenPolitico" ).item( 0 ).getTextContent() );
			// layoutXml.setCodigoUnidOrcamentaria(
			// element.getElementsByTagName( "aux:codigoUnidOrcamentaria"
			// ).item( 0 ).getTextContent() );
			// NodeList childNodes = document.getElementsByTagName(
			// "aux:numeroAtoQueNomeGestor" );
			//
			// List<GestorXml> gestores=new ArrayList<GestorXml>();
			//
			// for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
			// Element element2 = (Element) childNodes.item( i2 );
			// GestorXml gestor=new GestorXml();
			//
			// gestor.setNumero(element2.getElementsByTagName( "gen:numero"
			// ).item( 0 ).getTextContent());
			// gestor.setAno(element2.getElementsByTagName( "gen:ano" ).item( 0
			// ).getTextContent());
			//
			// gestores.add(gestor);
			// }
			//
			// layoutXml.setGestores(gestores);
			// NodeList childNodes = element.getChildNodes();

			// layoutXmlList.add( layoutXml );
			// }

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

	public EmpresaEnum[] getEmpresas() {
		return EmpresaEnum.values();
	}

	public MesEnum[] getMeses() {
		return MesEnum.values();
	}

	public UploadedFile getArquivoUploadFile() {
		return arquivoUploadFile;
	}

	public void setArquivoUploadFile(UploadedFile arquivoUploadFile) {
		this.arquivoUploadFile = arquivoUploadFile;
	}

	public InputText getArquivoNomeFileUpload() {
		return arquivoNomeFileUpload;
	}

	public void setArquivoNomeFileUpload(InputText arquivoNomeFileUpload) {
		this.arquivoNomeFileUpload = arquivoNomeFileUpload;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	

	public String getExercicio() {
		return exercicio;
	}

	public void setExercicio(String exercicio) {
		this.exercicio = exercicio;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}
	
//
//	@Override
//	public void onTabChanged(TabChangeEvent event) {
//	}
	

}
