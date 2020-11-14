package net.marcoreis.ecommerce.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.marcoreis.ecommerce.entidades.Produto;

public class AlterarPrecoProdutoTest {
	private EntityManager em;
	private List<Produto> produtos;

	@Before
	public void inicializar() {
		em = JPAUtil.getInstance().getEntityManager();
		produtos = em.createQuery("select p from Produto p",
				Produto.class).getResultList();
	}

	@After
	public void finalizar() {
		em.close();
	}

	@Test
	public void testAlterarPrecos() throws InterruptedException {
		for (Produto p : produtos) {
			em.getTransaction().begin();
			double preco =
					ThreadLocalRandom.current().nextDouble(500);
			p.setPreco(BigDecimal.valueOf(preco));
			p.setDataAtualizacao(new Date());
			em.persist(p);
			em.getTransaction().commit();
		}
	}
}
