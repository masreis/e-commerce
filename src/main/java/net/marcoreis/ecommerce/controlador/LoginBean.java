package net.marcoreis.ecommerce.controlador;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import net.marcoreis.ecommerce.entidades.Cliente;
import net.marcoreis.ecommerce.negocio.ClienteService;
import net.marcoreis.ecommerce.util.IPersistente;
import org.apache.log4j.Logger;

@SessionScoped
@ManagedBean
public class LoginBean extends BaseBean {
	private static final long serialVersionUID =
			4169068378414919948L;

	protected static final Logger logger =
			Logger.getLogger(LoginBean.class);

	private boolean loggedIn;

	private ClienteService clienteService = new ClienteService();

	public String login() {
		this.cliente = this.clienteService
				.carregarCliente(getCliente().getEmail());
		if (this.cliente != null) {
			setCliente(this.cliente);
			setLoggedIn(true);
			this.cliente.setUltimoLogin(new Date());
			this.clienteService
					.salvar((IPersistente) this.cliente);
			return "inicio";
		}
		setLoggedIn(false);
		errorMsg("Usuário inválido");
		return null;
	}

	@PostConstruct
	public void init() {
		setCliente(new Cliente());
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}
}
