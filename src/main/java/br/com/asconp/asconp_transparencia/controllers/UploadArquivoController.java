package br.com.asconp.asconp_transparencia.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.asconp.asconp_transparencia.entities.LayoutXml;
import br.com.asconp.asconp_transparencia.enums.EmpresaEnum;
import br.com.asconp.asconp_transparencia.enums.MesEnum;

@ManagedBean(name = "UploadArquivoController")
@ViewScoped
public class UploadArquivoController extends BaseController {

	private UploadedFile arquivoUploadFile;

	private InputText arquivoNomeFileUpload;

	private EmpresaEnum empresaEnum;

	private MesEnum mesEnum;

	public List<LayoutXml> layoutXmlList;

	private String login, password;

	@PostConstruct
	public void init() {
		empresaEnum = null;
		mesEnum = null;
	}

	public UploadArquivoController() {
		LoginBean controllerInstance = UtilsView
				.getControllerInstance(LoginBean.class);
		if (controllerInstance.getUserName() == null
				|| controllerInstance.getPassword() == null){
			//|| controllerInstance.getUserName() != "asconp" || controllerInstance.getPassword() != "asconp2017") {
		
			FacesContext context = FacesContext.getCurrentInstance();
			try {
				context.getExternalContext().redirect(
						"/asconp_transparencia/login.xhtml");
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
						"Arquivos importados com sucesso!");
				FacesContext.getCurrentInstance().addMessage(null, message);
				init();
			}
		}
		return;
	}

	private boolean salvarArquivo(UploadedFile uploadedFile) {

		String numeroMes = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			String caminho = servletContext.getRealPath(File.separator);

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(cal.get(Calendar.YEAR))
					.append(File.separator);

			if (!Paths.get(filePath.toString()).toFile().exists()) {
				new File(filePath.toString()).mkdir();
			}
			filePath.append(empresaEnum.getCodigo()).append(File.separator);
			// StringUtils.leftPad(mesEnum.ordinal()+1, 2, "0")
			numeroMes = String.format("%02d", mesEnum.ordinal() + 1);
			if (!Paths.get(filePath.toString()).toFile().exists()) {
				new File(filePath.toString()).mkdir();
			}
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
		// verificar o conteudo do arquivo se a empresa selecionada corresponde
		// a empresa do arquivo
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			String caminho = servletContext.getRealPath(File.separator);

			StringBuilder filePath = new StringBuilder();
			filePath.append(caminho).append(cal.get(Calendar.YEAR))
					.append(File.separator).append(empresaEnum.getCodigo())
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
				String urlDir = cal.get(Calendar.YEAR) + "" + File.separator
						+ empresaEnum.getCodigo() + File.separator + numeroMes
						+ File.separator;
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

		return true;
	}

	public List<LayoutXml> getLayoutXmlList() {
		layoutXmlList = new ArrayList<LayoutXml>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			String property = System.getProperty("user.home");
			String dir = System.getProperty("wtp.deploy")
					+ "/asconp_transparencia";
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

	@Override
	public void onTabChanged(TabChangeEvent event) {
	}

}
