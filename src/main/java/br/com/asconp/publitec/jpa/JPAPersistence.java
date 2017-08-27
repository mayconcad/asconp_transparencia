package br.com.asconp.publitec.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class JPAPersistence {

	private static EntityManager factory;

	private JPAPersistence() {}

	public static EntityManager getEntityManager() {
		if ( factory == null ) {
			//o valor lancamentoPU é referente a unidade de persistencia configurado no arquvo de conexão com banco de dados persistence.xml
			factory = Persistence.createEntityManagerFactory( "publitecPU" ).createEntityManager();
		}
		return factory;
	}
}
