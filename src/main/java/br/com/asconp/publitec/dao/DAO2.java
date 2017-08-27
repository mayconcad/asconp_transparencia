package br.com.asconp.publitec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

//interface parao dao
public interface DAO2<T> extends Serializable {

	T criar( T entidade );

	T editar( T entidade );

	T carregar( long id, Class<T> klass );

	List<T> buscar( Class<T> klass, Map<String, Object> params );

	T buscar( Class<T> klass, long id );

	void remover( long id );
}
