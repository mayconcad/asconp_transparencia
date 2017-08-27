package br.com.asconp.publitec.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import br.com.asconp.publitec.entities.BaseEntity;
import br.com.asconp.publitec.enums.DBNameEnum;
import br.com.asconp.publitec.exceptions.IPServidorDBException;

public interface IConexaoJdbcService {
	
	Connection conectarDB(DBNameEnum bancoDeDados, String ip, String porta,String usuario, String senha ) throws ClassNotFoundException, SQLException, IPServidorDBException;
	
	boolean conexaoEstaAberta(Connection con);
	
	void fecharConexao(Connection con);
	
	boolean executarComandoSQL(Connection con,String sql) throws SQLException;
	boolean executarComandoSQL(Connection con,String sql, Properties properties) throws SQLException;

	ResultSet listarBySQL(Connection con,String sql);
	
	<T extends BaseEntity>  List<T> listarObjetoSQL(Connection con,String sql, Class<T> className);
	
	
}
