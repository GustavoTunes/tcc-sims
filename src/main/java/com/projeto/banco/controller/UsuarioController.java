package com.projeto.banco.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.banco.model.Usuario;
import com.projeto.banco.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	private UsuarioService usuarioService;

	@Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
	
		@GetMapping("/novo")
		public String cadastrarUsuario(Model model) {
			model.addAttribute("usuarioForm", new Usuario());
			return "/cadastro";
		}
	
		/*@PostMapping("/cadastrar")
		public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model) {
			System.out.println(usuario.toString());
			model.addAttribute("usuarioForm", new Usuario());
			usuarioService.saveUsuario(usuario);
			return "login";
		}
		
		@PostMapping("/cadastrar")
		public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario){
			return new ResponseEntity<Usuario>(usuarioService.saveUsuario(usuario), HttpStatus.CREATED);
		}*/
	
	@PostMapping("/cadastrar")
		public String cadastrarUsuario(@Validated Usuario usuario, BindingResult result, RedirectAttributes attributes) {
	    // Salvar o usuário no banco de dados
	    usuarioService.saveUsuario(usuario);

	    // Redirecionar para a página de login
	   attributes.addFlashAttribute("usuarioCadastrado", true);
	    return "redirect:/login";  // Redireciona para a página de login
	}

		
		@GetMapping
		public List<Usuario> getAllUsuarios(){
			return usuarioService.getAllUsuarios();
		}
		
		@GetMapping("{id}")
		public ResponseEntity<Usuario> getUsuarioById(@PathVariable("id") long usuarioId){
			return new ResponseEntity<Usuario>(usuarioService.getUsuarioById(usuarioId), HttpStatus.OK);
		}
		
		@PutMapping("/alterar")
		public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") long id
													  ,@RequestBody Usuario usuario){
			return new ResponseEntity<Usuario>(usuarioService.updateUsuario(usuario, id), HttpStatus.OK);
		}
		
		@DeleteMapping("{id}")
		public ResponseEntity<String> deleteUsuario(@PathVariable("id") long id){
			
			usuarioService.deleteUsuario(id);
			
			return new ResponseEntity<String>("Usuário deletado com sucesso!", HttpStatus.OK);
		}
	
}
