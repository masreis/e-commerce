package net.marcoreis.ecommerce.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConfiguracaoTemporizador
		implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		(new UtilIndiceAutoCompletar())
				.criaIndexInfixAutoCompletar();
		(new TemporizadorIndiceProduto()).iniciar();
		(new TemporizadorIndiceVenda()).iniciar();
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}
