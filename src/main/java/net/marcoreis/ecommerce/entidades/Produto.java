package net.marcoreis.ecommerce.entidades;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.marcoreis.ecommerce.util.IPersistente;

@Entity
@NamedQueries({
		@NamedQuery(name = "produto.consultaTotal",
				query = "select count(p) from Produto p"),
		@NamedQuery(name = "produto.consultaPorDescricao",
				query = "select p from Produto p where p.descricao like :descricaoParcial"),
		@NamedQuery(name = "produto.consultaPorIntervaloPreco",
				query = "select p from Produto p where p.preco >= ?1 and p.preco <= ?2") })
public class Produto implements IPersistente {
	private static final long serialVersionUID =
			3206252406240046848L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String descricao;

	@Column(precision = 10, scale = 2)
	private BigDecimal preco;

	private boolean ativo;

	@Lob
	@Column(length = 5242880)
	private byte[] especificacaoFabricante;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ProdutoCategoria",
			joinColumns = { @JoinColumn(name = "produto_id") },
			inverseJoinColumns = {
					@JoinColumn(name = "categoria_id") })
	private Set<Categoria> categorias = new HashSet<>(0);

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtualizacao;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public byte[] getEspecificacaoFabricante() {
		return this.especificacaoFabricante;
	}

	public void setEspecificacaoFabricante(
			byte[] especificacaoFabricante) {
		this.especificacaoFabricante = especificacaoFabricante;
	}

	public BigDecimal getPreco() {
		return this.preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Set<Categoria> getCategorias() {
		return this.categorias;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Date getDataAtualizacao() {
		return this.dataAtualizacao;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isAtivo() {
		return this.ativo;
	}
}
