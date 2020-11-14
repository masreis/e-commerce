package net.marcoreis.ecommerce.negocio;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import net.marcoreis.ecommerce.entidades.Venda;
import net.marcoreis.ecommerce.util.JPAUtil;

public class VendaService extends GenericService<Venda> {

	private static final long serialVersionUID =
			5121260394627422657L;

	public List<Venda> consultarAtualizacoes(
			int tempoEmMinutos) {
		String sql =
				"select p from Venda p where dataAtualizacao between :inicio and :fim";
		EntityManager em =
				JPAUtil.getInstance().getEntityManager();
		TypedQuery<Venda> query =
				em.createQuery(sql, Venda.class);
		Date agora = new Date();
		Date inicio = new Date(
				agora.getTime() - tempoEmMinutos * 60 * 1000);
		query.setParameter("inicio", inicio);
		query.setParameter("fim", agora);
		query.setLockMode(LockModeType.NONE);
		query.setHint("org.hibernate.readOnly", true);
		query.setHint("org.hibernate.fetchSize", 10000);
		List<Venda> vendas = query.getResultList();
		em.close();
		return vendas;
	}

}
