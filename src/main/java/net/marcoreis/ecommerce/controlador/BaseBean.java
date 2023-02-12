package net.marcoreis.ecommerce.controlador;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import net.marcoreis.ecommerce.entidades.Cliente;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseBean implements Serializable {
	private static final long serialVersionUID =
			-5895396595360064610L;

	protected static final Logger logger =
			LogManager.getLogger(BaseBean.class);

	protected static final String MENSAGEM_SUCESSO_GRAVACAO =
			"Dados gravados com sucesso";

	protected Cliente cliente;

	protected void infoMsg(String message) {
		FacesMessage msg = new FacesMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	protected void errorMsg(Throwable t) {
		logger.error(t);
		FacesMessage msg =
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						t.getMessage(), "Erro");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	protected void errorMsg(String message) {
		FacesMessage msg = new FacesMessage(
				FacesMessage.SEVERITY_ERROR, message, "Erro");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String getParametro(String parametro) {
		return (String) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap()
				.get(parametro);
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return this.cliente;
	}
}
