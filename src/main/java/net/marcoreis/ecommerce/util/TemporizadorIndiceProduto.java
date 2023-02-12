package net.marcoreis.ecommerce.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TemporizadorIndiceProduto implements Runnable {
	private static Logger logger =
			LogManager.getLogger(TemporizadorIndiceProduto.class);

	private final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(1);

	public void iniciar() {
		scheduler.scheduleAtFixedRate(this, 1, 1,
				TimeUnit.MINUTES);
	}

	public void run() {
		IndexadorProduto indexador = new IndexadorProduto();
		try {
			logger.info("Iniciando temporizador "
					+ Thread.currentThread().getName());
			indexador.inicializar();
			int um_minuto = 1;
			indexador.atualizarIndice(um_minuto);
			indexador.fechar();
			logger.info("Finalizando temporizador "
					+ Thread.currentThread().getName());
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
