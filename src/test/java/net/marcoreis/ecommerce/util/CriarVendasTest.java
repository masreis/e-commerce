package net.marcoreis.ecommerce.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.marcoreis.ecommerce.entidades.Cliente;
import net.marcoreis.ecommerce.entidades.ItemVenda;
import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.entidades.Venda;

public class CriarVendasTest {
	private static final int QUANTIDADE_VENDAS = 10000;
	private EntityManager em;
	private List<Produto> produtos;
	private List<Cliente> clientes;

	@Before
	public void inicializar() {
		em = JPAUtil.getInstance().getEntityManager();
		produtos = em.createQuery("select p from Produto p",
				Produto.class).getResultList();
		clientes = em.createQuery("select c from Cliente c",
				Cliente.class).getResultList();
	}

	@After
	public void finalizar() {
		em.close();
	}

	@Test
	public void testCriarVendas() {
		// Quantidade de vendas de teste
		for (int i = 0; i < QUANTIDADE_VENDAS; i++) {
			em.getTransaction().begin();
			Venda venda = new Venda();
			// Cada venda tem até 10 itens aleatórios
			int qtdItensVenda = ThreadLocalRandom.current()
					.nextInt(1, 10 + 1);
			venda.setCliente(getClienteAleatorio());
			venda.setData(getDataAleatoria());
			venda.setDataAtualizacao(new Date());
			venda.setAtivo(true);
			for (int j = 0; j < qtdItensVenda; j++) {
				ItemVenda itemVenda = new ItemVenda();
				Produto produto = getProdutoAleatorio();
				itemVenda.setProduto(produto);
				// Cada item pode ter até 10 unidades na venda
				int qtdItem =
						ThreadLocalRandom.current().nextInt(10);
				itemVenda.setQuantidade(qtdItem);
				double total = produto.getPreco().doubleValue()
						* qtdItem;
				itemVenda.setValorTotal(
						BigDecimal.valueOf(total));
				itemVenda.setValorUnitario(produto.getPreco());
				venda.getItensVenda().add(itemVenda);
			}
			em.persist(venda);
			em.getTransaction().commit();
		}
	}

	private Produto getProdutoAleatorio() {
		return produtos.get(ThreadLocalRandom.current()
				.nextInt(0, produtos.size() - 1));
	}

	private Date getDataAleatoria() {
		return new Date();
	}

	private Cliente getClienteAleatorio() {
		return clientes.get(ThreadLocalRandom.current()
				.nextInt(0, clientes.size()));
	}

}
