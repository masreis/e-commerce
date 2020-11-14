package net.marcoreis.ecommerce.controlador;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.lucene.document.Document;

import net.marcoreis.ecommerce.util.LuceneLazyDataModel;
import net.marcoreis.ecommerce.util.TipoIndice;
import net.marcoreis.ecommerce.util.UtilIndice;

@ManagedBean
@ViewScoped
public class BuscaLivreProdutoBean extends BaseBean {
	private static final long serialVersionUID =
			-7508553590263034662L;

	private String consulta;

	private LuceneLazyDataModel docs;

	private TipoIndice tipo = TipoIndice.PRODUTO;

	private List<String> sugestoesProduto;

	public LuceneLazyDataModel getDocs() {
		if (this.docs == null)
			this.docs = new LuceneLazyDataModel(getConsulta(),
					getTipo());
		return this.docs;
	}

	public void setDocs(LuceneLazyDataModel docs) {
		this.docs = docs;
	}

	public void consultar() {
		this.docs = new LuceneLazyDataModel(getConsulta(),
				getTipo());
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public String getConsulta() {
		return this.consulta;
	}

	public BigDecimal getDuracaoBusca() {
		Double d = Double
				.valueOf(getDocs().getDuracaoBusca() / 1000.0D);
		BigDecimal bd =
				(new BigDecimal(d.doubleValue())).setScale(4, 2);
		return bd;
	}

	public String getDescricaoFormatada() {
		return ((Document) getDocs().getRowData())
				.get("produtoDescricao")
				.replaceAll("\n", "<br />");
	}

	public String abrirPaginaBusca() {
		return "consultar";
	}

	public TipoIndice getTipo() {
		return this.tipo;
	}

	public List<String> carregarSugestoesProduto(
			String nomeParcial) {
		try {
			return UtilIndice.getInstancia(getTipo())
					.buscaSugestoesProduto(nomeParcial);
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}
}
