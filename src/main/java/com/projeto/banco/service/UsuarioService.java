package com.projeto.banco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.banco.model.Usuario;

@Service
public interface UsuarioService {
	Usuario saveUsuario(Usuario usuario);
	List<Usuario> getAllUsuarios();
	Usuario getUsuarioById(long id);
	Usuario updateUsuario(Usuario usuario, long id);
	void deleteUsuario(long id);
}
