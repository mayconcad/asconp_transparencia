package br.com.asconp.publitec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.asconp.publitec.entities.BaseEntity;
import br.com.asconp.publitec.vos.BaseVO;

public interface DAO extends Serializable {

	<V extends BaseVO> V create(BaseEntity entity, Class<V> voClass);

	<V extends BaseVO> V update(BaseEntity entity, Class<V> voClass);

	<V extends BaseVO> V load(BaseEntity entity, Class<V> voClass);

	<V extends BaseVO> List<V> find(Class<? extends BaseEntity> klass,
			Map<String, Object> params, Class<V> voClass, boolean clausuleOR);
	<V extends BaseVO> List<V> find(Class<? extends BaseEntity> klass,
			 Class<V> voClass, String sqlAfteWhere);

	<V extends BaseVO> List<V> autocomplete(String namedQuery,
			Class<V> voClass, Object... parameters);

	void delete(BaseEntity entity);
}
