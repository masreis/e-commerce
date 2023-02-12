package net.marcoreis.ecommerce.controlador;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;

import net.marcoreis.ecommerce.entidades.Categoria;
import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.negocio.ProdutoService;
import net.marcoreis.ecommerce.util.IPersistente;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ViewScoped
@ManagedBean
public class ProdutoBean extends BaseBean {
	private static final long serialVersionUID =
			-6475971812078805662L;

	private static Logger logger =
			LogManager.getLogger(ProdutoBean.class);

	private Produto produto;

	private ProdutoService produtoService = new ProdutoService();

	private Collection<Produto> produtos;

	private UploadedFile especificacaoFabricante;

	private UploadedFile foto;

	public void preRender() {
		logger.info("=> preRender()");
	}

	@PostConstruct
	public void init() {
		this.produto = new Produto();
		carregarProdutos();
	}

	public void carregarProdutos() {
		this.produtos = this.produtoService
				.carregarColecao(Produto.class);
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Produto getProduto() {
		return this.produto;
	}

	public Collection<Produto> getProdutos() {
		return this.produtos;
	}

	public void salvar() {
		try {
			if (getEspecificacaoFabricante() != null
					&& getEspecificacaoFabricante()
							.getSize() > 0L) {
				byte[] dados = IOUtils
						.toByteArray(getEspecificacaoFabricante()
								.getInputstream());
				getProduto().setEspecificacaoFabricante(dados);
			}
			getProduto().setDataAtualizacao(new Date());
			this.produtoService
					.salvar((IPersistente) getProduto());
			infoMsg("Dados gravados com sucesso");
		} catch (Exception e) {
			errorMsg(e);
		}
	}

	public void setEspecificacaoFabricante(
			UploadedFile especificacaoFabricante) {
		this.especificacaoFabricante = especificacaoFabricante;
	}

	public UploadedFile getEspecificacaoFabricante() {
		return this.especificacaoFabricante;
	}

	public String editar(Produto produto) {
		this.produto = produto;
		return "produto?faces-redirect=true&amp;includeViewParams=true";
	}

	public void excluir(Produto produto) {
		try {
			this.produtoService.remove((IPersistente) produto);
			carregarProdutos();
			infoMsg("Produto exclu" + produto.getNome());
		} catch (Exception e) {
			errorMsg(e.getLocalizedMessage());
		}
	}

	public void carregarProdutosPorCategoria(
			ValueChangeEvent evento) {
		String filtro = "categoria.id = ?1";
		String newValue = evento.getNewValue().toString();
		Long idCategoria =
				Long.valueOf(Long.parseLong(newValue));
		this.produtos = this.produtoService.carregarColecao(
				Produto.class, filtro,
				new Object[] { idCategoria });
	}

	public void setFoto(UploadedFile foto) {
		this.foto = foto;
	}

	public UploadedFile getFoto() {
		return this.foto;
	}

	public StreamedContent getDownloadEspecificacaoFabricante() {
		return (StreamedContent) new DefaultStreamedContent(
				new ByteArrayInputStream(getProduto()
						.getEspecificacaoFabricante()),
				"application/pdf", "arquivo.pdf");
	}

	public boolean isExisteFoto() {
		return false;
	}

	public String categorias(Produto produto) {
		return produto.getCategorias().stream()
				.map(Categoria::getNome)
				.collect(Collectors.joining(","));
	}

}
