package net.marcoreis.ecommerce.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import net.marcoreis.ecommerce.entidades.Categoria;
import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.negocio.ProdutoService;

public class IndexadorProduto {
	private static Logger logger =
			Logger.getLogger(IndexadorProduto.class);

	private ProdutoService produtoService = new ProdutoService();

	private Tika tika = new Tika();

	private UtilIndice utilIndice;

	public void indexarProdutos()
			throws IOException, TikaException {
		List<Produto> produtos = this.produtoService
				.carregarColecao(Produto.class);
		for (Produto prod : produtos)
			indexarProduto(prod);
	}

	private void indexarProduto(Produto produto)
			throws IOException, TikaException {
		Document doc = new Document();
		StringBuilder textoCompleto = new StringBuilder();
		preencherDadosProduto(produto, doc, textoCompleto);
		preencherDadosCategoria(produto, doc, textoCompleto);
		preencherDadosTextoCompleto(doc, textoCompleto);
		preencherDescricaoVetor(produto, doc);
		this.utilIndice.atualizarDoc(new Term("produtoId",
				produto.getId().toString()), doc, false);
	}

	private void preencherDadosTextoCompleto(Document doc,
			StringBuilder textoCompleto) {
		doc.add((IndexableField) new TextField("textoCompleto",
				textoCompleto.toString(), Field.Store.YES));
	}

	private void preencherDadosCategoria(Produto produto,
			Document doc, StringBuilder textoCompleto) {
		for (Categoria categoria : produto.getCategorias()) {
			doc.add((IndexableField) new TextField(
					"categoriaNome", categoria.getNome(),
					Field.Store.YES));
			textoCompleto.append(" ");
			textoCompleto.append(categoria.getNome());
		}
	}

	private void preencherDescricaoVetor(Produto produto,
			Document doc) {
		FieldType tipoComPosicoes = new FieldType();
		tipoComPosicoes.setIndexOptions(
				IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		tipoComPosicoes.setStored(true);
		tipoComPosicoes.setStoreTermVectorOffsets(true);
		tipoComPosicoes.setStoreTermVectorPayloads(true);
		tipoComPosicoes.setStoreTermVectorPositions(true);
		tipoComPosicoes.setStoreTermVectors(true);
		String descricao = produto.getDescricao();
		if (descricao == null)
			descricao = produto.getNome();
		doc.add((IndexableField) new Field(
				"produtoDescricaoVetor", descricao,
				tipoComPosicoes));
	}

	private void preencherDadosProduto(Produto produto,
			Document doc, StringBuilder textoCompleto)
			throws IOException, TikaException {
		doc.add((IndexableField) new StringField("produtoId",
				produto.getId().toString(), Field.Store.YES));
		String descricao = (produto.getDescricao() == null) ? ""
				: produto.getDescricao();
		doc.add((IndexableField) new TextField(
				"produtoDescricao", descricao, Field.Store.YES));
		doc.add((IndexableField) new TextField("produtoNome",
				produto.getNome(), Field.Store.YES));
		doc.add((IndexableField) new StringField("produtoNomeNA",
				produto.getNome(), Field.Store.YES));
		String especFabricante = getEspecProduto(produto);
		if (!"".equals(especFabricante))
			doc.add((IndexableField) new TextField(
					"especFabricante", especFabricante,
					Field.Store.YES));
		doc.add((IndexableField) new TextField("produtoPreco",
				produto.getPreco().toString(), Field.Store.YES));
		doc.add((IndexableField) new DoublePoint(
				"produtoPrecoPoint", new double[] {
						produto.getPreco().doubleValue() }));
		doc.add((IndexableField) new TextField("dataAtualizacao",
				DateTools.dateToString(
						produto.getDataAtualizacao(),
						DateTools.Resolution.MINUTE),
				Field.Store.YES));
		textoCompleto.append(" ");
		textoCompleto.append(produto.getNome());
		if (!"".equals(especFabricante)) {
			textoCompleto.append(" ");
			textoCompleto.append(especFabricante);
		}
		textoCompleto.append(" ");
		textoCompleto.append(produto.getDescricao());
	}

	public String getEspecProduto(Produto produto)
			throws IOException, TikaException {
		if (produto.getEspecificacaoFabricante() != null) {
			ByteArrayInputStream bytes =
					new ByteArrayInputStream(produto
							.getEspecificacaoFabricante());
			return this.tika.parseToString(bytes);
		}
		return "";
	}

	public void atualizarIndice(int tempoEmMinutos)
			throws IOException, TikaException {
		List<Produto> produtos = this.produtoService
				.consultarAtualizacoes(tempoEmMinutos);
		logger.info(String.valueOf(produtos.size())
				+ " produtos alterados");
		for (Produto produto : produtos) {
			if (produto.isAtivo()) {
				indexarProduto(produto);
				continue;
			}
			removerProdutoIndice(produto);
		}
		logger.info("Indexaconclu[Produto]");
	}

	private void removerProdutoIndice(Produto produto)
			throws IOException {
		this.utilIndice.removerDoc("produtoId",
				produto.getId().toString());
	}

	public void fechar() throws IOException {
		this.utilIndice.fechar();
	}

	public void inicializar() throws IOException {
		this.utilIndice =
				UtilIndice.getInstancia(TipoIndice.PRODUTO);
	}
}
