package projeto.sims.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Table(name = "usuario")
@Entity(name = "Usuario")
public class Usuario implements UserDetails, Serializable{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(name = "nome_completo", nullable = false)
	private String nomeCompleto;
	
	@Column(name = "nome_usuario", nullable = false)
	private String nomeUsuario;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "senha", nullable = false)
	private String senha;
	
	@Column(name = "data_nascimento", nullable = false)
	private Date dataNascimento;

	public Usuario(String nomeCompleto, String nomeUsuario, String email, Date dataNascimento, String senha) {
		super();
		this.nomeCompleto = nomeCompleto;
		this.nomeUsuario = nomeUsuario;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.senha = senha;
	}

	public Usuario() {
	}
	
	public Usuario(String nomeCompleto, String nomeUsuario, String email, String senha, Date dataNascimento) {
		super();
		this.nomeCompleto = nomeCompleto;
		this.nomeUsuario = nomeUsuario;
		this.email = email;
		this.senha = senha;
		this.dataNascimento = dataNascimento;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    // Retorna uma autoridade comum para todos os usuários
	    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
       
}