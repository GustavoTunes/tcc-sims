package projeto.sims.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.servlet.ModelAndView;
import projeto.sims.model.AuthenticationDTO;
import projeto.sims.model.RegisterDTO;
import projeto.sims.model.Usuario;
import projeto.sims.repository.UsuarioRepository;
import projeto.sims.service.TokenService;
import projeto.sims.service.UsuarioService;

@RestController
public class UsuarioController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping("/entrar")
	public ModelAndView entrar(@Valid AuthenticationDTO data, HttpSession session) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		var token = tokenService.generateToken((Usuario) auth.getPrincipal());

		if (auth.isAuthenticated()) {
			session.setAttribute("token", token);
			boolean usuarioLogado = auth.isAuthenticated();
			ModelAndView modelAndView = new ModelAndView("index");
			modelAndView.addObject("usuarioLogado", usuarioLogado);
			modelAndView.addObject("nome", "João");
			return modelAndView;
		}

		return new ModelAndView("login");
	}
	
	@PostMapping("/cadastrar")
    public void cadastrarUsuario(@Valid RegisterDTO data, HttpServletResponse response) throws IOException {
		if(usuarioRepository.findByEmail(data.email()) != null) response.sendRedirect("/falhou");
		
		String encryptedSenha = new BCryptPasswordEncoder().encode(data.senha());
		
		Usuario novoUsuario = new Usuario(data.nomeCompleto(), data.nomeUsuario(), data.email(), encryptedSenha, data.dataNascimento());
		
		this.usuarioRepository.save(novoUsuario);

		response.sendRedirect("/");
	}
	
	/*public void cadastrarUsuario(Usuario usuario, HttpServletResponse response) throws IOException {
        Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
        //return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
        response.sendRedirect("/");
    }*/
	
	 @GetMapping("/usuarios")
	    public ResponseEntity<List<Usuario>> listarUsuarios() {
	        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
	        return new ResponseEntity<>(usuarios, HttpStatus.OK);
	    }

	 @GetMapping("/meu-perfil")
	 public String teste() {
return "olá";
	 }
	 
/*	    @GetMapping("/{id}")
	    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
	        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
	        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
	                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	    }*/

	    @DeleteMapping("/deletar")
	    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
	        usuarioService.deletarUsuario(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	}
	


