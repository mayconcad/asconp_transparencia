package br.com.asconp.asconp_transparencia.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class UtilsView {

	/**
	 * Limpa os dados dos componentes de edição e de seus filhos,
	 * recursivamente. Checa se o componente é instância de EditableValueHolder
	 * e 'reseta' suas propriedades.
	 * <p>
	 * Quando este método, por algum motivo, não funcionar, parta para
	 * ignorância e limpe o componente assim:
	 * <p>
	 * <blockquote>
	 * 
	 * <pre>
	 * component.getChildren().clear()
	 * </pre>
	 * 
	 * </blockquote> :-)
	 *
	 * 
	 */
	public static void cleanSubmittedValues(UIComponent component) {
		if (component instanceof EditableValueHolder) {
			EditableValueHolder evh = (EditableValueHolder) component;
			evh.setSubmittedValue(null);
			evh.setValue(null);
			evh.setLocalValueSet(false);
			evh.setValid(true);
		}
		// Dependendo de como se implementa um Composite Component, ele retorna
		// ZERO
		// na busca por filhos. Nesse caso devemos iterar sobre os componentes
		// que o
		// compõe de forma diferente.
		if (UIComponent.isCompositeComponent(component)) {
			Iterator i = component.getFacetsAndChildren();
			while (i.hasNext()) {
				UIComponent comp = (UIComponent) i.next();

				// TODO: isolar em um método?
				if (comp.getChildCount() > 0) {
					for (UIComponent child : comp.getChildren()) {
						cleanSubmittedValues(child);
					}
				}
			}
		}
		// TODO: isolar em um método?
		if (component.getChildCount() > 0) {
			for (UIComponent child : component.getChildren()) {
				cleanSubmittedValues(child);
			}
		}
	}

	// converte o arquivo em um array de bytes
	public static byte[] imageToByte(String caminhoArquivo) throws IOException {
		InputStream is = null;
		byte[] buffer = null;
		is = new FileInputStream(caminhoArquivo);
		buffer = new byte[is.available()];
		is.read(buffer);
		is.close();
		return buffer;
	}

	public static <T extends BaseController> T getControllerInstance(
			Class<T> klass) {
		FacesContext currentInstance = FacesContext.getCurrentInstance();

		T newInstance = klass.cast(currentInstance
				.getELContext()
				.getELResolver()
				.getValue(
						currentInstance.getELContext(),
						null,
						klass.getSimpleName().substring(0, 1).toLowerCase()
								+ klass.getSimpleName().substring(1)));

		return newInstance;
	}

	public static FileInputStream getStreamLogoPHB() {
		FacesContext context = FacesContext.getCurrentInstance();
		// trás a localizacao fixa do servidor e não a temporaria
		ServletContext servletContext = (ServletContext) context
				.getExternalContext().getContext();
		FileInputStream logoInputStream = null;
		try {
			logoInputStream = new FileInputStream(
					new File(
							servletContext
									.getRealPath("/resources/images/bandeira-parnaiba.300x225.jpg")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return logoInputStream;
	}
}
