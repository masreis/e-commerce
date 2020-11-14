package net.marcoreis.ecommerce.util;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.junit.Test;

public class IndexadorProdutoTest {
	@Test
	public void testIndexarTodosProdutos()
			throws IOException, TikaException {
		IndexadorProduto indexador = new IndexadorProduto();
		indexador.inicializar();
		indexador.indexarProdutos();
		indexador.fechar();
	}
}
