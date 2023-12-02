package com.projeto.banco.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import lombok.Data;

//@Data
@Entity
@Table(name="usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "nome_completo")
	private String nomeCompleto;
	
	@Column(name = "nome_usuario")
	private String nomeUsuario;
	
	private String email;
	private String senha;
	
	@Column(name = "data_nascimento")
	private Date dataNascimento;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nomeCompleto=" + nomeCompleto + ", nomeUsuario=" + nomeUsuario + ", email="
				+ email + ", senha=" + senha + ", dataNascimento=" + dataNascimento + "]";
	}

	
	
}