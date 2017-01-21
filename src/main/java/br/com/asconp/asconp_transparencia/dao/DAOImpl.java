package br.com.asconp.asconp_transparencia.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import br.com.asconp.asconp_transparencia.jpa.JPAPersistence;

//@Component
//implementação para dao
public class DAOImpl<T> implements DAO<T> {

	private static final long serialVersionUID = 4843607362465868178L;

	protected EntityManager manager;

	//aqui faz-se a chamada para o entitymanager já estabelecendo a conexão com o banco de dados
	//e daqui pra frente o hibernate faz os inserts e updates no banco via sql
	public DAOImpl() {
		manager = JPAPersistence.getEntityManager();
	}

	@Override
	public T criar( T entidade ) {

		try {
			manager.getTransaction().isActive();
			manager.getTransaction().begin();
			manager.persist( entidade );
			manager.getTransaction().commit();
		} catch ( IllegalArgumentException e ) {
			throw new IllegalArgumentException( "" );

		} catch ( TransactionRequiredException e ) {
			throw new TransactionRequiredException( "" );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return entidade;
	}

	@Override
	public T editar( T entidade ) {
		try {
			manager.getTransaction().begin();
			manager.merge( entidade );
			manager.getTransaction().commit();
		} catch ( IllegalArgumentException e ) {
			throw new IllegalArgumentException( "" );

		} catch ( TransactionRequiredException e ) {
			throw new TransactionRequiredException( "" );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return entidade;
	}

	@Override
	public T carregar( long id, Class<T> klass ) {

		T entidade = buscar( klass, id );

		try {
			manager.getTransaction().begin();
			manager.refresh( entidade );
			manager.getTransaction().commit();

		} catch ( IllegalArgumentException e ) {
			throw new IllegalArgumentException( "" );

		} catch ( TransactionRequiredException e ) {
			throw new TransactionRequiredException( "" );
		} catch ( EntityNotFoundException e ) {
			throw new EntityNotFoundException( "" );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return entidade;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public List<T> buscar( Class<T> klass, Map<String, Object> params ) {
		List<T> list = null;
		try {
			manager.getTransaction().isActive();
			manager.getTransaction().begin();
			StringBuffer sb = new StringBuffer();
			String result = null;
			sb.append( "SELECT x FROM " + klass.getSimpleName() + " x " );
			if ( params.size() > 0 ) {
				sb.append( " WHERE " );
				for ( String map : params.keySet() ) {
					if ( map != null ) {
						sb.append( "x." + map + " = :" + map + " AND " );
					}
				}
				result = sb.substring( 0, sb.length() - 4 );
			} else {
				result = sb.toString();
			}

			// "select x from aluno x where x.nomeDoAtributo = :nomeDoAtributo AND x.1 = :1 AND x.2 = :2 AND...";

			Query query = manager.createQuery( result );

			for ( String map : params.keySet() ) {
				if ( !params.keySet().isEmpty() ) {
					query.setParameter( map, params.get( map ) );
				}
			}
			list = query.getResultList();
			manager.getTransaction().commit();
		} catch ( Exception e ) {
			e.printStackTrace();

		}

		return list;
	}

	@Override
	public T buscar( Class<T> klass, long id ) {
		Query query = manager.createQuery( "SELECT x FROM " + klass.getName() + " x WHERE x.id = :id" );
		query.setParameter( "id", id );
		T object = (T) query.getSingleResult();

		return object;
	}

	@Override
	public void remover( long id ) {
		manager.getTransaction().begin();
		manager.remove( id );
		manager.getTransaction().commit();
	}

	public void rollback() {
		manager.getTransaction().rollback();
		manager.close();
	}
}