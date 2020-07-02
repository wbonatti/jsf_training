package br.com.wellington.cartaocredito.model.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConnectionFactory {

	private static EntityManagerFactory factory;

	public static synchronized EntityManager getInstance() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("cartao-credito");
		}
		return factory.createEntityManager();
	}

}
