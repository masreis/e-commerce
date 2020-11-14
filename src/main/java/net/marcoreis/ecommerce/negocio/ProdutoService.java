package net.marcoreis.ecommerce.negocio;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.util.JPAUtil;

public class ProdutoService extends GenericService<Produto> {

	private static final long serialVersionUID =
			-2527941294512350117L;

	/**
	 * Retorna os registros atualizados/inseridos recentemente
	 * 
	 * @param tempo
	 *            quantidade de minutos
	 * @return lista de produtos alterados nos últimos minutos
	 */
	public List<Produto> consultarAtualizacoes(
			int tempoEmMinutos) {
		String sql =
				"select p from Produto p where dataAtualizacao between :inicio and :fim";
		EntityManager em =
				JPAUtil.getInstance().getEntityManager();
		TypedQuery<Produto> query =
				em.createQuery(sql, Produto.class);
		Date agora = new Date();
		Date inicio = new Date(
				agora.getTime() - tempoEmMinutos * 60 * 1000);
		query.setParameter("inicio", inicio);
		query.setParameter("fim", agora);
		List<Produto> produtos = query.getResultList();
		em.close();
		return produtos;
	}

}
