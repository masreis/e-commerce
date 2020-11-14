package net.marcoreis.ecommerce.controlador;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.marcoreis.ecommerce.entidades.Categoria;
import net.marcoreis.ecommerce.negocio.CategoriaService;
import net.marcoreis.ecommerce.util.IPersistente;

@ManagedBean
@ViewScoped
public class CategoriaBean extends BaseBean {
	private static final long serialVersionUID =
			861905629535769221L;

	private Categoria categoria;

	private CategoriaService categoriaService =
			new CategoriaService();

	private Collection<Categoria> categorias;

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public Collection<Categoria> getCategorias() {
		return this.categorias;
	}

	@PostConstruct
	public void init() {
		carregarCategorias();
		this.categoria = new Categoria();
	}

	public void salvar() {
		try {
			this.categoriaService
					.salvar((IPersistente) getCategoria());
			infoMsg("Dados gravados com sucesso");
		} catch (Exception e) {
			errorMsg(e);
		}
	}

	public void carregarCategorias() {
		this.categorias = this.categoriaService
				.carregarColecao(Categoria.class);
	}

	public void excluir(Categoria categoria) {
		try {
			this.categoriaService
					.remove((IPersistente) categoria);
			carregarCategorias();
			infoMsg("Categoria exclu" + categoria.getNome());
		} catch (Exception e) {
			errorMsg(e.getMessage());
		}
	}
}
