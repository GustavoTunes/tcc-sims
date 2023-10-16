package com.projeto.banco.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.banco.exception.ResourceNotFoundException;
import com.projeto.banco.model.Usuario;
import com.projeto.banco.repository.UsuarioRepository;
import com.projeto.banco.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository usuarioRepository;

	@Autowired
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
		
		Usuario existingUsuario = usuarioRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Usuario", "Id", id)); 
		
		existingUsuario.setNomeCompleto(existingUsuario.getNomeCompleto());
		
		usuarioRepository.save(existingUsuario);
		return existingUsuario;
	}
	

	@Override
	public void deleteUsuario(long id) {
		
		usuarioRepository.findById(id).orElseThrow(() -> 
								new ResourceNotFoundException("Usuario", "Id", id));
		usuarioRepository.deleteById(id);
	}
	
}
