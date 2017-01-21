package br.com.asconp.asconp_transparencia.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import br.com.asconp.asconp_transparencia.entities.BaseEntity;
import br.com.asconp.asconp_transparencia.enums.DBNameEnum;
import br.com.asconp.asconp_transparencia.enums.PackageDBEnum;
import br.com.asconp.asconp_transparencia.exceptions.IPServidorDBException;

public class ConexaoJdbcService implements IConexaoJdbcService {

	private Connection connection = null;

	@Override
	public Connection conectarDB(DBNameEnum bancoDeDados, String ip,
			String porta, String usuario, String senha)
			throws ClassNotFoundException, SQLException, IPServidorDBException {

		switch (bancoDeDados) {
		case MYSQL:
			if (ip == null)// aqui devera implementar comando pra verificar ip
							// ativo 3306 root mysql
				throw new IPServidorDBException("MySQL");
			Class.forName(PackageDBEnum.MYSQL_DRIVER.getDescricao());
			System.out.println("Foi criada a conexão ao DB MYSQL");
			connection = DriverManager.getConnection(PackageDBEnum.MYSQL_DRIVER
					.formatPrefixConnection(ip, porta), usuario, senha);
			break;
		case POSTGRESQL:
			if (ip != null) {
				Runtime run = Runtime.getRuntime();
				try {
					run.exec("ping " + ip);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new IPServidorDBException("Postgres");
				}
				// aqui devera implementar comando pra verificar ip
				// ativo 3306 root mysql
			}
			Class.forName(PackageDBEnum.POSTGRES_DRIVER.getDescricao());
			System.out.println("Foi criada a conexão ao DB POSTGRES");
			connection = DriverManager.getConnection(
					PackageDBEnum.POSTGRES_DRIVER.formatPrefixConnection(ip,
							porta), usuario, senha);
			break;
		case ORACLE:
			if (ip != null) {
				Runtime run = Runtime.getRuntime();
				try {
					run.exec("ping " + ip);
				} catch (IOException e) {
					e.printStackTrace();
					throw new IPServidorDBException("Oracle");
				}
			}
			Class.forName(PackageDBEnum.POSTGRES_DRIVER.getDescricao());
			System.out.println("Foi criada a conexão ao DB oracle");
			connection = DriverManager.getConnection(
					PackageDBEnum.POSTGRES_DRIVER.formatPrefixConnection(ip,
							porta), usuario, senha);
			break;
		default:
			System.out
					.println("VOcê informou um banco de dados diferente de POSTGRES e MYSQL");
			return null;
		}
		return connection;

	}

	@Override
	public boolean conexaoEstaAberta(Connection con) {
		try {
			return con != null && !con.isClosed() ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void fecharConexao(Connection con) {
		try {
			if (con != null && !con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet listarBySQL(Connection con, String sql) {

		try {
			if (con != null && !con.isClosed()) {
				Statement prepareStatement = con.createStatement();
						//ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet resultSet = prepareStatement.executeQuery(sql);

				return resultSet;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private <T extends BaseEntity> T populateObjectByClass(Class<T> className,
			Map<String, String> parameterMap) {

		Map<String, String> newMap = new HashMap<String, String>();
		T entity = null;
		try {

			//for (String key : parameterMap.keySet()) {

				//newMap.put(key, parameterMap.get(key));
			//}
			entity = className.newInstance();
			//BeanUtils.populate(entity, newMap);
		} catch (IllegalAccessException 
				| InstantiationException e1) {
			e1.printStackTrace();
			throw new IllegalArgumentException();
		}
		return entity;
	}

	@Override
	public boolean executarComandoSQL(Connection con, String sql)
			throws SQLException {
		boolean executado = true;
		try {
			if (con != null && !con.isClosed()) {

			
				ResultSet rs = this.listarBySQL(con, "SELECT MAX(id) FROM aluno ");
				
				int maiorId =0;
				while(rs.next()){
				
					 maiorId = rs.getInt(1);
				}

				PreparedStatement prepareStatement = con.prepareStatement(sql);
				prepareStatement.setInt(1, maiorId+1);
				prepareStatement.setString(2, "Aluno prparestatment");
				prepareStatement.setInt(3, 1);
				
				
				ResultSet rsc = this.listarBySQL(con, "SELECT id FROM curso WHERE nomecurso LIKE 'Portugues'");
				while(rsc.next()){
					
					 maiorId = rsc.getInt(1);
				}
				
				prepareStatement.setInt(4, maiorId);

				prepareStatement.execute();
				prepareStatement.close();
			}
		} catch (Exception e) {
			executado = false;
		}
		return executado;
	}

	@Override
	public boolean executarComandoSQL(Connection con, String sql,
			Properties properties) throws SQLException {

		Statement prepareStatement = con.createStatement();
		int resultSet = prepareStatement.executeUpdate(sql);

		return resultSet == 1 ? true : false;
	}

	@Override
	public <T extends BaseEntity> List<T> listarObjetoSQL(Connection con,
			String sql, Class<T> className) {
		List<T> list = new ArrayList<T>();
		try {
			if (conexaoEstaAberta(con)) {
				Map<String, String> mapFieldClass = new HashMap<String, String>();
				Statement prepareStatement = connection.createStatement();

				ResultSet resultSet = prepareStatement.executeQuery(sql);

				//
				ResultSetMetaData dataTable = resultSet.getMetaData();
				int columnCount = dataTable.getColumnCount();
				while (resultSet.next()) {
					mapFieldClass.clear();
					for (; columnCount > 0; columnCount--) {

						String columnName = dataTable
								.getColumnName(columnCount);
						if (columnName.contains("_id")
								|| columnName.contains("version"))
							continue;
						mapFieldClass.put(
								columnName.contains("_id") ? columnName
										.replace("_", ".") : columnName, String
										.valueOf(resultSet
												.getObject(columnCount)));
					}
					list.add(populateObjectByClass(className, mapFieldClass));
				}

				prepareStatement.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void insereNovoRegistro(String sql) throws SQLException {
		Statement prepareStatement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = prepareStatement.executeQuery("");
		rs.absolute(1);

		// rs.updatestr

	}

}
