package br.com.asconp.publitec.controllers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

//classe com funcionalidades comuns a todos os controllers e que ser estendida por eles
public abstract class BaseController implements Serializable {

	private static final long serialVersionUID = 891153748337320506L;

	protected Connection conexaoBanco = null;
	protected Statement createStatement = null;
	protected ResultSet result;
	
	private DataTable searchResults;

	private Tab editTab;
	private Tab findTab;

	private int activeTabIndex = 0;

//	public void closeEditTab( ActionEvent actionEvent ) {
//		if ( getEditTab() != null ) {
//			getEditTab().setRendered( false );
//			TabView parent = (TabView) getEditTab().getParent();
//			int editIndex = parent.getChildren().indexOf( getEditTab() );
//			// parent.getChildren().remove(editIndex);
//			parent.setActiveIndex( editIndex - 1 );
//			Ajax.update( parent.getId() );
//		}
//	}

//	public abstract void onTabChanged( TabChangeEvent event );

	// {
	// TabView tv = (TabView) event.getComponent();
	// this.activeTabIndex = tv.getActiveIndex();
	// }

//	private void setUpDataTable( DataTable table ) {
//		table.setLazy( true );
//		table.setPaginator( true );
//		table.setPaginatorPosition( "bottom" );
//		table.setEmptyMessage( "Não foram encontrados registros" );
//		table.setRows( 20 );
//		table.setVar( "item" );
//		table.setRowsPerPageTemplate( "20,40,60" );
		//table.setPaginatorTemplate( ResourceBundleUtils.getLocalizedMessage( "table.paginatorTemplate" ) );
		//table.setCurrentPageReportTemplate( ResourceBundleUtils.getLocalizedMessage( "table.currentPageReportTemplate" ) );
//	}
	
//	public void setSearchResults( DataTable searchResults ) {
//		this.searchResults = searchResults;
//		setUpDataTable( this.searchResults );
//	}
//	
//	public DataTable getSearchResults() {
//		return searchResults;
//	}

	public Tab getEditTab() {
		return editTab;
	}

	public void setEditTab( Tab editTab ) {
		this.editTab = editTab;
	}

	public Tab getFindTab() {
		return findTab;
	}

	public void setFindTab( Tab findTab ) {
		this.findTab = findTab;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex( int activeTabIndex ) {
		this.activeTabIndex = activeTabIndex;
	}

	protected void addErrorMessage( String componentId, String errorMessage ) {
		addMessage( componentId, errorMessage, FacesMessage.SEVERITY_ERROR );
	}

	protected void addErrorMessage( String errorMessage ) {
		addErrorMessage( null, errorMessage );
	}

	protected void addInfoMessage( String componentId, String infoMessage ) {
		addMessage( componentId, infoMessage, FacesMessage.SEVERITY_INFO );
	}

	protected void addInfoMessage( String infoMessage ) {
		addInfoMessage( null, infoMessage );
	}

	private void addMessage( String componentId, String errorMessage, Severity severity ) {
		FacesMessage message = new FacesMessage( errorMessage );
		message.setSeverity( severity );
		FacesContext.getCurrentInstance().addMessage( componentId, message );
	}
}