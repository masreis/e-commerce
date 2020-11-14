package net.marcoreis.ecommerce.entidades;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import net.marcoreis.ecommerce.util.IPersistente;

@Entity
@NamedQuery(name = "usuario.consultaAcessoDia",
		query = "select c from Cliente c where c.ultimoLogin = :data")
public class Cliente implements IPersistente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String nome;

	private Date ultimoLogin;

	private static final long serialVersionUID =
			5073165906294726127L;

	@Column(unique = true, nullable = false)
	private String cpfCnpj;

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getCpfCnpj() {
		return this.cpfCnpj;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return this.nome;
	}

	public void setUltimoLogin(Date ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}

	public Date getUltimoLogin() {
		return this.ultimoLogin;
	}
}
