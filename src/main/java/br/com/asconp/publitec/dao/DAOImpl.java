package br.com.asconp.publitec.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import br.com.asconp.publitec.entities.BaseEntity;
import br.com.asconp.publitec.jpa.JPAPersistence;
import br.com.asconp.publitec.vos.BaseVO;

//@Component
public class DAOImpl implements DAO {

	private static final long serialVersionUID = 4843607362465868178L;

	protected EntityManager manager;

	public DAOImpl() {
		manager = JPAPersistence.getEntityManager();
	}

	@Override
	public <V extends BaseVO> V create(BaseEntity entity, Class<V> voClass) {

		try {
//			if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
			manager.persist(entity);
			manager.getTransaction().commit();
			//manager.close();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("");

		} catch (TransactionRequiredException e) {
			throw new TransactionRequiredException("");
		} catch (Exception e) {
			e.printStackTrace();
		}

		V voInstance = converterToVO(entity, voClass);

		return voInstance;
	}

	@Override
	public <V extends BaseVO> V update(BaseEntity entity, Class<V> voClass)
			{
		try {
//			if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
			manager.merge(entity);
			manager.getTransaction().commit();
			//manager.close();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("");

		} catch (TransactionRequiredException e) {
			throw new TransactionRequiredException("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		V voInstance = converterToVO(entity, voClass);

		return voInstance;
	}

	@Override
	public <V extends BaseVO> V load(BaseEntity entity, Class<V> voClass) {
		try {
//			if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
			manager.refresh(entity);
			manager.getTransaction().commit();
			//manager.close();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("");

		} catch (TransactionRequiredException e) {
			throw new TransactionRequiredException("");
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		V voInstance = converterToVO(entity, voClass);

		return voInstance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends BaseVO> List<V> find(Class<? extends BaseEntity> klass,
			Map<String, Object> params, Class<V> voClass, boolean clausuleOR) {
		List<V> list = null;
		try {
//			if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
			StringBuffer sb = new StringBuffer();
			String result = null;
			sb.append("SELECT x FROM " + klass.getSimpleName() + " x ");
			if (params.size() > 0) {
				sb.append(" WHERE ");
				for (String map : params.keySet()) {
					if (map != null) {
						if(clausuleOR)
							sb.append("x." + map +" like :"+ map + " OR ");
						else
						sb.append("x." + map + " like :" +map + " AND ");
					}
				}
				result = sb.substring(0, sb.length() - 4);
			} else {
				result = sb.toString();
			}

			// "select x from aluno x where x.nomeDoAtributo = :nomeDoAtributo AND x.1 = :1 AND x.2 = :2 AND...";

			Query query = manager.createQuery(result);

			for (String map : params.keySet()) {
				if (!params.keySet().isEmpty()) {
					query.setParameter(map, params.get(map));
				}
			}
			list = query.getResultList();
			manager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

		}
		//manager.close();
		return list;
	}

	@Override
	public <V extends BaseVO> List<V> autocomplete(String namedQuery,
			Class<V> voClass, Object... parameters) {
		try {
			Query query = manager.createNamedQuery(namedQuery);
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					query.setParameter(i + 1, parameters[i]);
				}
			}
			@SuppressWarnings("unchecked")
			List<V> list = query.getResultList();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void delete(BaseEntity entity) {
//		if(!manager.getTransaction().isActive())
		manager.getTransaction().begin();
		manager.remove(entity);
		manager.getTransaction().commit();

	}

	public void rollback() {
		manager.getTransaction().rollback();
		manager.close();
	}

	private <V extends BaseVO> V converterToVO(BaseEntity entity, Class<V> vo) {

		V voInstance=null;
		try {
			voInstance = vo.newInstance();
			for (Field fieldVO : voInstance.getClass().getDeclaredFields()) {
				for (Field fieldClass : entity.getClass().getDeclaredFields()) {
					if (fieldVO.getName().equals(fieldClass.getName())) {
						fieldVO.set(voInstance, fieldClass.get(entity));
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return voInstance;
	}

	@Override
	public <V extends BaseVO> List<V> find(Class<? extends BaseEntity> klass,
			Class<V> voClass, String sqlAfteWhere) {
		List<V> list = null;
		try {
			if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
			StringBuffer sb = new StringBuffer();
			
			sb.append("SELECT x FROM " + klass.getSimpleName() + " x WHERE ");			
			sb.append(sqlAfteWhere);

			Query query = manager.createQuery(sb.toString());
			
			list = query.getResultList();
			manager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return list;
	}

	@Override
	public <V extends BaseEntity> List<V> find(
			Class<? extends BaseEntity> klass, String sqlAfteWhere) {
		List<V> list = null;
		try {
			if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
			StringBuffer sb = new StringBuffer();
			
			sb.append("SELECT x FROM " + klass.getSimpleName() + " x WHERE ");			
			sb.append(sqlAfteWhere);

			Query query = manager.createQuery(sb.toString());
			
			list = query.getResultList();
			manager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return list;
	}

	@Override
	public void closeClonection() {
		manager.close();
	}

}
