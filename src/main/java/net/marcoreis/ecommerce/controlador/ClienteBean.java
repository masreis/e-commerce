package net.marcoreis.ecommerce.controlador;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import net.marcoreis.ecommerce.entidades.Cliente;
import net.marcoreis.ecommerce.negocio.ClienteService;

@ViewScoped
@ManagedBean
public class ClienteBean extends BaseBean {
	private static final long serialVersionUID =
			-2658024901938874346L;

	private Collection<Cliente> clientes;

	private ClienteService clienteService = new ClienteService();

	@PostConstruct
	public void init() {
		carregarClientes();
		this.cliente = new Cliente();
	}

	public void salvar() {
	}

	public void carregarClientes() {
		this.clientes = this.clienteService
				.carregarColecao(Cliente.class);
	}

	public Collection<Cliente> getClientes() {
		return this.clientes;
	}

	public void setClientes(Collection<Cliente> clientes) {
		this.clientes = clientes;
	}
}
