package net.marcoreis.ecommerce.util;

import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import net.marcoreis.ecommerce.entidades.Cliente;

public class CriarClientesTest {

	private static final int QUANTIDADE_CLIENTES = 1000;
	private EntityManager em;

	@Before
	public void inicializar() {
		em = JPAUtil.getInstance().getEntityManager();
	}

	@Test
	public void testCriarClientes() {
		for (int i = 0; i < QUANTIDADE_CLIENTES; i++) {
			em.getTransaction().begin();
			Cliente cliente = new Cliente();
			String val = String.valueOf(ThreadLocalRandom
					.current().nextInt(1000, 1000000000));
			cliente.setCpfCnpj(val);
			cliente.setEmail("email" + i + "@gmail.com");
			cliente.setNome("Nome " + i);
			em.persist(cliente);
			em.getTransaction().commit();
		}
	}
}
