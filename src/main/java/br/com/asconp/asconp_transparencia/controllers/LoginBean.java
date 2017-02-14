package br.com.asconp.asconp_transparencia.controllers;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.TabChangeEvent;

@ManagedBean
//@RequestScoped
@SessionScoped
public class LoginBean extends BaseController {

	private static final long serialVersionUID = 4661688222410469654L;

	//@ManagedProperty(value = "#{authenticationService}")
	///private AuthenticationService authenticationService;

	//@ManagedProperty(value = "#{userServiceImpl}")
	//private UserService userServiceImpl;
	
	private String login;

	private String userName;
	private String password;

	private String principalRoleUser;

	private boolean error = false;

	//private User currentUser = new User();

	//@ManagedProperty(value = "#{liquidacaoServiceJob}")
	//LiquidacaoService liquidacaoServiceJob;
	

	 public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@PostConstruct
	 public void init() {
	// try {
	// LiquidacaoJobController controllerInstance = UtilsView
	// .getControllerInstance(LiquidacaoJobController.class);
	// controllerInstance.agendar();
	// } catch (Exception e) {
	// e.printStackTrace();
	 }
	// }

	public void login() {
		CaptchaBean controllerInstance = UtilsView
				.getControllerInstance(CaptchaBean.class);
		// authenticationService.logout();
		if (!controllerInstance.validarCaptcha())
			return;

		boolean success = false;
		
			
		error = false;

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();

		String url = externalContext.getRequestContextPath();
		
		if(getUserName() != null && getUserName().equals("asconp") && getPassword() != null && getPassword().equals("asconp2017")){
			try {
//				if (url != null
//						&& url.equals(File.separator.concat("pages")
//								.concat(File.separator).concat("index.xhtml")))
//					url = "/asconp_transparencia";
				context.getExternalContext().redirect("/asconp_transparencia/pages/upload/Upload.xhtml");
				System.out.println(url);
				// context.getExternalContext().redirect(url +
				// "/pages/index.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Login ou senha inválidos", "Login ou senha inválidos"));
		//if (!success) {
			//this.error = true;

			
			
			//return;
			// return "falhaLogin";
		//}

		
		

		return;
		// return "sucessoLogin";
	}

	public void logoutLogin(ActionEvent actionEvent) {
		
	}

	public void logout() {
	
		limparControllers();

		FacesContext context = FacesContext.getCurrentInstance();
		String url = context.getExternalContext().getRequestContextPath();

		// SecurityContextHolder.getContext().setAuthentication(null);
		// SecurityContextHolder.clearContext();
		try {
			context.getExternalContext().redirect(url + "/login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return "login";
	}

	public void logout(boolean redirect) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		String url = context.getExternalContext().getRequestContextPath();

		if (redirect)
			context.getExternalContext().redirect(url + "/login.xhtml");
	}

	public void recuperarSenha() {
		FacesContext context = FacesContext.getCurrentInstance();
		String url = context.getExternalContext().getRequestContextPath();

		try {

			context.getExternalContext().redirect(url + "/recuperaSenha.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void limparControllers() {
		LoginBean loginBean = UtilsView
				.getControllerInstance(LoginBean.class);
		loginBean.init();	

	}

	public String[] getCurrentUser() {
		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("username", ((User) SecurityContextHolder.getContext()
		// .getAuthentication().getPrincipal()).getUsername());
		// List<User> list = userServiceImpl.buscar(params);
		// return list == null ? new User() : list.get(0);
		//if (currentUser == null || currentUser.getId() == null) {
		//	currentUser = ((User) SecurityContextHolder.getContext()
		//			.getAuthentication().getPrincipal());
		//}
		return new String[]{};
	}

	public String getPrincipalRole() {

		if (getPrincipalRoleUser() == null) {
		//	Authentication auth = SecurityContextHolder.getContext()
				//	.getAuthentication();
			//if (auth == null) {
				FacesContext context = FacesContext.getCurrentInstance();
				String url = context.getExternalContext()
						.getRequestContextPath();
				try {
					context.getExternalContext().redirect(url);
					System.out.println(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "";
		//	}
		//	User user = (User) auth.getPrincipal();
		//	setPrincipalRoleUser(user.getRoles().iterator().next().getName());

			//return getPrincipalRoleUser();
		}
		return getPrincipalRoleUser();
	}

	private void redirectForContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		String url = context.getExternalContext().getRequestContextPath();
		try {
			context.getExternalContext().redirect(url + "/pages/index.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getUserName() {
		if (userName == null) {
			//Authentication auth = SecurityContextHolder.getContext()
					//.getAuthentication();
			//return auth == null || auth.getName().equals("anonymousUser") ? null
					//: auth.getName();
		}
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
	
	public String getPrincipalRoleUser() {
		return principalRoleUser;
	}

	public void setPrincipalRoleUser(String principalRoleUser) {
		this.principalRoleUser = principalRoleUser;
	}

	@Override
	public void onTabChanged(TabChangeEvent event) {

	}
}