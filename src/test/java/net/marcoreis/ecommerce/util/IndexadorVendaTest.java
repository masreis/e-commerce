package net.marcoreis.ecommerce.util;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.junit.Test;

public class IndexadorVendaTest {
	@Test
	public void testIndexarTodasVendas()
			throws IOException, TikaException {
		IndexadorVenda indexador = new IndexadorVenda();
		indexador.inicializar();
		indexador.indexarVendas();
		indexador.fechar();
	}
}
