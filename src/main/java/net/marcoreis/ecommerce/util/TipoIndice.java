package net.marcoreis.ecommerce.util;

public enum TipoIndice {
	PRODUTO(String.valueOf(System.getProperty("user.home"))
			+ "/livro-lucene/indice-produto"),
	VENDA(String.valueOf(System.getProperty("user.home"))
			+ "/livro-lucene/indice-venda"),
	PRODUTO_AUTOCOMPLETAR(String
			.valueOf(System.getProperty("user.home"))
			+ "/livro-lucene/indice-produto-autocompletar");

	private String diretorio;

	TipoIndice(String diretorio) {
		this.diretorio = diretorio;
	}

	public String diretorio() {
		return this.diretorio;
	}
}
