package com.projeto.banco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.banco.model.Usuario;
import com.projeto.banco.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}
	
	// build create employee REST API
		@PostMapping()
		public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario){
			return new ResponseEntity<Usuario>(usuarioService.saveUsuario(usuario), HttpStatus.CREATED);
		}
		
		// build get all employees REST API
		@GetMapping
		public List<Usuario> getAllUsuarios(){
			return usuarioService.getAllUsuarios();
		}
		
		// build get employee by id REST API
		// http://localhost:8080/api/employees/1
		@GetMapping("{id}")
		public ResponseEntity<Usuario> getUsuarioById(@PathVariable("id") long usuarioId){
			return new ResponseEntity<Usuario>(usuarioService.getUsuarioById(usuarioId), HttpStatus.OK);
		}
		
		// build update employee REST API
		// http://localhost:8080/api/employees/1
		@PutMapping("{id}")
		public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") long id
													  ,@RequestBody Usuario usuario){
			return new ResponseEntity<Usuario>(usuarioService.updateUsuario(usuario, id), HttpStatus.OK);
		}
		
		// build delete employee REST API
		// http://localhost:8080/api/employees/1
		@DeleteMapping("{id}")
		public ResponseEntity<String> deleteUsuario(@PathVariable("id") long id){
			
			// delete employee from DB
			usuarioService.deleteUsuario(id);
			
			return new ResponseEntity<String>("Usuário deletado com sucesso!", HttpStatus.OK);
		}
	
}
