package br.com.asconp.asconp_transparencia.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.com.asconp.asconp_transparencia.dao.DAO;
import br.com.asconp.asconp_transparencia.dao.DAOImpl;
import br.com.asconp.asconp_transparencia.entities.Lancamento;
import br.com.asconp.asconp_transparencia.entities.Pessoa;
import br.com.asconp.asconp_transparencia.entities.TipoLancamento;

//anotação para indicar que se trata de um bean gerenciável, ou seja, classe que receberá as requisições vindas da tela
@ManagedBean
//aqui o escopo do controller, isto é, o tempo de vida do objeto, ao se fechar a tela de lancamento ete objeto deixa de exisitir, por isso possui escopo 
//de visão
@ViewScoped
public class LancamentoController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2779462563851472552L;

	// Atributos para criar o objeto
	private Lancamento lancamento;
	private DAO dao;

	// Atributos para buscar os objetos
	private String descricao;
	private List<Lancamento> lancamentos;

	// Atributo remover
	private Lancamento lancamentoRemove;

	//contrutor que instancia o dao
	public LancamentoController() {
		dao = new DAOImpl<Lancamento>();
		lancamentos = new ArrayList<Lancamento>();
		// Pessoa p = new Pessoa();
		// p.setCpf( "00822480360" );
		// p.setEndereco( "Avenida Amadeus Paulo, 2543, Bairro: Santa Rosa" );
		// p.setNome( "Maycon Silva" );
		// p.setRg( "2302931" );
		//
		// p = (Pessoa) dao.criar( p );
		//
		// Lancamento l1 = new Lancamento();
		// l1.setDataBaixa( new Date() );
		// l1.setDataVencimento( new Date() );
		// l1.setDescricao( "Lançamento Número Um" );
		//
		// l1.setPessoa( p );
		// l1.setTipo( TipoLancamento.RECEITA );
		// l1.setValor( new BigDecimal( 150.00 ) );
		// l1.setDescricao( "Licença de Software" );
		//
		// l1 = (Lancamento) dao.criar( l1 );
		//
		// Lancamento l2 = new Lancamento();
		// l2.setDataBaixa( new Date() );
		// l2.setDataVencimento( new Date() );
		// l2.setDescricao( "Lançamento Número Dois" );
		//
		// l2.setPessoa( p );
		// l2.setTipo( TipoLancamento.RECEITA );
		// l2.setValor( new BigDecimal( 150.00 ) );
		// l2.setDescricao( "Treinamento da Equipe" );
		//
		// l2 = (Lancamento) dao.criar( l2 );
		//
		// lancamentos.add( l1 );
		// lancamentos.add( l2 );
	}

	@PostConstruct
	public void init() {
		lancamento = new Lancamento();
	}

	//metodo para criar lançamentos
	public void criar() {
		Pessoa pessoa = criarPessoa();
		lancamento.setDataBaixa( new Date() );
		lancamento.setPessoa( pessoa );
		dao.criar( lancamento );

		addInfoMessage( "Cadastro Realizado com Sucesso" );
		init();
		buscar();
		RequestContext.getCurrentInstance().execute( "lancamentoCreateDialog.hide();" );
		RequestContext.getCurrentInstance().update( "lancamentoTabView:searchForm" );
		// return "listaLancamento?faces-redirect=true";
	}

	private Pessoa criarPessoa() {
		Pessoa p = new Pessoa();
		p.setCpf( "00822480360" );
		p.setEndereco( "Avenida Amadeus Paulo, 2543, Bairro: Santa Rosa" );
		p.setNome( "Maycon Silva" );
		p.setRg( "2302931" );

		p = (Pessoa) dao.criar( p );
		return p;
	}

	//metodos para listas lancamentos
	public void buscar() {
		lancamentos.clear();
		Map<String, Object> params = new HashMap<String, Object>();
		if ( getDescricao() != null && !getDescricao().isEmpty() )
			params.put( "descricao", getDescricao() );
		List lista = dao.buscar( Lancamento.class, params );
		lancamentos.addAll( lista);
	}

	//metodo usado no compoenete selectOneMenu
	public TipoLancamento[] getTipos() {
		return TipoLancamento.values();
	}

	@Override
	public void onTabChanged( TabChangeEvent event ) {

	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento( Lancamento lancamento ) {
		this.lancamento = lancamento;
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao( DAO dao ) {
		this.dao = dao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao( String descricao ) {
		this.descricao = descricao;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos( List<Lancamento> lancamentos ) {
		this.lancamentos = lancamentos;
	}

	public Lancamento getLancamentoRemove() {
		return lancamentoRemove;
	}

	public void setLancamentoRemove( Lancamento lancamentoRemove ) {
		this.lancamentoRemove = lancamentoRemove;
	}

}
