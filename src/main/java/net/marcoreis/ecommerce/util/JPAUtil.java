package net.marcoreis.ecommerce.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JPAUtil {
	private static EntityManagerFactory emf = null;
	private static JPAUtil instance;
	private static Logger logger =
			LogManager.getLogger(JPAUtil.class);

	static {
		instance = new JPAUtil();
		emf = Persistence
				.createEntityManagerFactory("e-commerce-pu");
	}

	public static JPAUtil getInstance() {
		return instance;
	}

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}
