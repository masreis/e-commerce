package net.marcoreis.ecommerce.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.marcoreis.ecommerce.util.IPersistente;

@Entity
public class Venda implements IPersistente {
	private static final long serialVersionUID =
			-4519913495960906821L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date data;

	@ManyToOne
	private Cliente cliente;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtualizacao;

	@OneToMany(cascade = { CascadeType.ALL },
			fetch = FetchType.EAGER)
	@JoinColumn(name = "venda_id")
	private List<ItemVenda> itensVenda = new ArrayList<>();

	private Boolean ativo;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getData() {
		return this.data;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Date getDataAtualizacao() {
		return this.dataAtualizacao;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public List<ItemVenda> getItensVenda() {
		return this.itensVenda;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getAtivo() {
		return this.ativo;
	}
}
