package br.com.asconp.asconp_transparencia.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.component.tabview.Tab;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.com.asconp.asconp_transparencia.dao.DAO;
import br.com.asconp.asconp_transparencia.dao.DAOImpl;
import br.com.asconp.asconp_transparencia.entities.BaseEntity;
import br.com.asconp.asconp_transparencia.entities.Lancamento;
import br.com.asconp.asconp_transparencia.entities.Pessoa;
import br.com.asconp.asconp_transparencia.entities.TipoLancamento;
import br.com.asconp.asconp_transparencia.enums.DBNameEnum;
import br.com.asconp.asconp_transparencia.exceptions.IPServidorDBException;
import br.com.asconp.asconp_transparencia.services.ConexaoJdbcService;
import br.com.asconp.asconp_transparencia.services.IConexaoJdbcService;

@ManagedBean(name = "SGBDController")
@ViewScoped
public class SGBDController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2779462563851472552L;

	// Atributos para criar o objeto
	private Lancamento lancamento;
	private DAO dao;

	// Atributos para buscar os objetos
	private List<Lancamento> lancamentos;

	// Atributo remover
	private Lancamento lancamentoRemove;

	private DBNameEnum sgbdSeleted;

	private String sql;

	private String resultSQL;

	private IConexaoJdbcService service;

	@PostConstruct
	public void init() {
		service = new ConexaoJdbcService();
		lancamento = new Lancamento();
	}

	// metodo para criar lançamentos
	public void criar() {
		Pessoa pessoa = criarPessoa();
		lancamento.setDataBaixa(new Date());
		lancamento.setPessoa(pessoa);
		dao.criar(lancamento);

		addInfoMessage("Cadastro Realizado com Sucesso");
		init();
		RequestContext.getCurrentInstance().execute(
				"lancamentoCreateDialog.hide();");
		RequestContext.getCurrentInstance().update(
				"lancamentoTabView:searchForm");
		// return "listaLancamento?faces-redirect=true";
	}

	private Pessoa criarPessoa() {
		Pessoa p = new Pessoa();
		p.setCpf("00822480360");
		p.setEndereco("Avenida Amadeus Paulo, 2543, Bairro: Santa Rosa");
		p.setNome("Maycon Silva");
		p.setRg("2302931");

		p = (Pessoa) dao.criar(p);
		return p;
	}

	public void executarSQL() {
		if (getSql() != null && !getSql().isEmpty())
			try {
				if (getSql().trim().substring(0, 5).equalsIgnoreCase("select")) {
					ResultSet resultSet = service.listarBySQL(null, getSql());
					resultSQL = prepareResult(resultSet);
				} else {
					service.executarComandoSQL(null, getSql());
				}
			} catch (SQLException e) {
				addErrorMessage("Ocorreu um erro inesperado, verifique sua query!");
				e.printStackTrace();
			}

	}

	private String prepareResult(ResultSet resultSet) throws SQLException {
		StringBuilder sb = new StringBuilder("");
		ResultSetMetaData dataTable = resultSet.getMetaData();
		int columnCount = dataTable.getColumnCount();
		long countRecords = 0;
		Map<String, List<String>> tabelaMap = new HashMap<String, List<String>>();

		while (resultSet.next()) {
			tabelaMap.clear();
			++countRecords;

			for (; columnCount > 0; columnCount--) {

				String columnName = dataTable.getColumnName(columnCount);
				String columnValue = String.valueOf(resultSet
						.getObject(columnCount));
				if (tabelaMap.containsKey(columnName)) {
					List<String> list = tabelaMap.get(columnName);
					list.add(columnValue);
				} else {
					List<String> valores = new ArrayList<String>();
					valores.add(columnValue);
					tabelaMap.put(columnName, valores);
				}
			}
		}

		Map<String, Integer> tamanhoColuna = prepareTamColuna(tabelaMap);
		
		for (String key : tamanhoColuna.keySet()) {
		
			sb.append(String.format(tamanhoColuna.get(key) + "%s", key));
		}

		sb.append("\n");
		//String.format(format, args)
		
		Map<String, String> linhaMap = new HashMap<String, String>();

		for (int x = 0; x < countRecords; x++) {

			for (String key : tabelaMap.keySet()) {
				
				List<String> list = tabelaMap.get(key);
				sb = sb.append(list.get(x)).append(" | ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	private Map<String, Integer> prepareTamColuna(
			Map<String, List<String>> tabelaMap) {
		Map<String, Integer> tamCampoMap = new HashMap<String, Integer>();

		for (String key : tabelaMap.keySet()) {
			List<String> list = tabelaMap.get(key);
			// pega o tamanho do maior valor
			int size = 0;
			for (String valor : list) {
				if (size < valor.length())
					size = valor.length();
			}
			tamCampoMap.put(key, size);

		}
		return tamCampoMap;
	}

	// metodo usado no compoenete selectOneMenu
	public TipoLancamento[] getTipos() {
		return TipoLancamento.values();
	}

	public DBNameEnum[] getSdbds() {
		return DBNameEnum.values();
	}

	public DBNameEnum getSgbdSeleted() {
		return sgbdSeleted;
	}

	public void setSgbdSeleted(DBNameEnum sGBDSeleted) {
		sgbdSeleted = sGBDSeleted;
	}

	// contrutor que instancia o dao
	public SGBDController() {
		dao = new DAOImpl<Lancamento>();
		lancamentos = new ArrayList<Lancamento>();
	}

	@Override
	public void onTabChanged(TabChangeEvent event) {

	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public String getResultSQL() {
		return resultSQL;
	}

	public void setResultSQL(String resultSQL) {
		this.resultSQL = resultSQL;
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public Lancamento getLancamentoRemove() {
		return lancamentoRemove;
	}

	public void setLancamentoRemove(Lancamento lancamentoRemove) {
		this.lancamentoRemove = lancamentoRemove;
	}

}
