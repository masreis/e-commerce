package net.marcoreis.ecommerce.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import net.marcoreis.ecommerce.entidades.ItemVenda;
import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.entidades.Venda;
import net.marcoreis.ecommerce.negocio.VendaService;

public class IndexadorVenda {
	private static Logger logger =
			Logger.getLogger(IndexadorVenda.class);

	private VendaService vendaService = new VendaService();

	private Tika tika = new Tika();

	private UtilIndice utilIndice;

	public void indexarVendas()
			throws IOException, TikaException {
		List<Venda> vendas =
				this.vendaService.carregarColecao(Venda.class);
		logger.info(String.valueOf(vendas.size())
				+ " vendas para indexa");
		for (Venda venda : vendas)
			indexarVenda(venda);
		logger.info("Indexacao Concluida [Venda]");
	}

	private void indexarVenda(Venda venda)
			throws IOException, TikaException {
		Document doc = new Document();
		StringBuilder textoCompleto = new StringBuilder();
		preencherDadosVenda(venda, doc);
		preencherDadosItemVenda(venda, doc, textoCompleto);
		preencherDadosCliente(venda, doc, textoCompleto);
		preencherDadosTextoCompleto(venda, doc, textoCompleto);
		this.utilIndice.atualizarDoc(
				new Term("vendaId", venda.getId().toString()),
				doc, false);
	}

	private void preencherDadosTextoCompleto(Venda venda,
			Document doc, StringBuilder textoCompleto) {
		doc.add((IndexableField) new TextField("textoCompleto",
				textoCompleto.toString(), Field.Store.YES));
	}

	private void preencherDadosVenda(Venda venda, Document doc) {
		doc.add((IndexableField) new StringField("vendaId",
				venda.getId().toString(), Field.Store.YES));
		doc.add((IndexableField) new TextField("dataAtualizacao",
				DateTools.dateToString(
						venda.getDataAtualizacao(),
						DateTools.Resolution.MINUTE),
				Field.Store.YES));
		doc.add((IndexableField) new TextField("data",
				DateTools.dateToString(venda.getData(),
						DateTools.Resolution.MINUTE),
				Field.Store.YES));
	}

	private void preencherDadosCliente(Venda venda, Document doc,
			StringBuilder textoCompleto) {
		doc.add((IndexableField) new StringField("clienteId",
				venda.getCliente().getId().toString(),
				Field.Store.YES));
		doc.add((IndexableField) new TextField("clienteNome",
				venda.getCliente().getNome(), Field.Store.YES));
		doc.add((IndexableField) new StringField("clienteEmail",
				venda.getCliente().getEmail(), Field.Store.YES));
		textoCompleto.append(" ");
		textoCompleto.append(venda.getCliente().getNome());
		textoCompleto.append(" ");
		textoCompleto.append(venda.getCliente().getEmail());
	}

	private void preencherDadosItemVenda(Venda venda,
			Document doc, StringBuilder textoCompleto)
			throws IOException, TikaException {
		for (ItemVenda iv : venda.getItensVenda()) {
			doc.add((IndexableField) new TextField(
					"itemQuantidade",
					iv.getQuantidade().toString(),
					Field.Store.YES));
			doc.add((IndexableField) new TextField(
					"itemValorUnitario",
					iv.getValorUnitario().toString(),
					Field.Store.YES));
			doc.add((IndexableField) new TextField(
					"itemValorTotal",
					iv.getValorTotal().toString(),
					Field.Store.YES));
			doc.add((IndexableField) new IntPoint(
					"itemQuantidadePoint", new int[] {
							iv.getQuantidade().intValue() }));
			doc.add((IndexableField) new DoublePoint(
					"itemValorUnitarioPoint",
					new double[] { iv.getValorUnitario()
							.doubleValue() }));
			doc.add((IndexableField) new DoublePoint(
					"itemValorTotalPoint", new double[] {
							iv.getValorTotal().doubleValue() }));
			preencherDadosProduto(iv.getProduto(), doc,
					textoCompleto);
		}
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

	public void fechar() throws IOException {
		this.utilIndice.fechar();
	}

	public void inicializar() throws IOException {
		this.utilIndice =
				UtilIndice.getInstancia(TipoIndice.VENDA);
	}

	public void atualizarIndice(int tempoEmMinutos)
			throws IOException, TikaException {
		List<Venda> vendas = this.vendaService
				.consultarAtualizacoes(tempoEmMinutos);
		logger.info(String.valueOf(vendas.size())
				+ " vendas para indexa");
		for (Venda venda : vendas)
			indexarVenda(venda);
		logger.info("Indexaconclu[Venda]");
	}
}
