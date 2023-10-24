package com.projeto.banco.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.banco.exception.ResourceNotFoundException;
import com.projeto.banco.model.Usuario;
import com.projeto.banco.repository.UsuarioRepository;
import com.projeto.banco.service.UsuarioService;

@Service
public class UsuarioServiceImpl {

	private UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public Usuario saveUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public List<Usuario> getAllUsuarios() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario getUsuarioById(long id) {

		return usuarioRepository.findById(id).orElseThrow(() -> 
						new ResourceNotFoundException("Usuario", "Id", id));
		
	}

	@Override
	public Usuario updateUsuario(Usuario usuario, long id) {
		
		// we need to check whether employee with given id is exist in DB or not
		Usuario existingUsuario = usuarioRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Usuario", "Id", id)); 
		
		existingUsuario.setNomeCompleto(existingUsuario.getNomeCompleto());
		// save existing employee to DB
		usuarioRepository.save(existingUsuario);
		return existingUsuario;
	}
	
/*
	@Override
	public void deleteUsuario(long id) {
		
		// check whether a employee exist in a DB or not
		usuarioRepository.findById(id).orElseThrow(() -> 
								new ResourceNotFoundException("Usuario", "Id", id));
		usuarioRepository.deleteById(id);
	}*/
	
}
