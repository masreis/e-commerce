package net.marcoreis.ecommerce.negocio;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import net.marcoreis.ecommerce.entidades.Cliente;
import net.marcoreis.ecommerce.util.JPAUtil;

public class ClienteService extends GenericService {
	public Cliente carregarCliente(String email) {
		EntityManager em = JPAUtil.getInstance()
				.getEntityManager();
		try {
			String sql = "select c from Cliente c where email = :email";
			TypedQuery<Cliente> query = em.createQuery(sql,
					Cliente.class);
			query.setParameter("email", email);
			Cliente cliente = query.getSingleResult();
			return cliente;
		} catch (Exception e) {
			return null;
		} finally {
			em.close();
		}
	}
}
