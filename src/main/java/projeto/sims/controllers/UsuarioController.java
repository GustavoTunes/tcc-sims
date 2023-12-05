package projeto.sims.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.servlet.ModelAndView;
import projeto.sims.model.Usuario;
import projeto.sims.model.UsuarioDTO;
import projeto.sims.repository.UsuarioRepository;
import projeto.sims.service.UsuarioService;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/home")
	public ModelAndView entrar(@ModelAttribute("usuario") Usuario usuario, Model model, HttpSession session) {

		Optional<Usuario> usuarioOptional = usuarioService.buscarUsuarioPorEmailESenha(usuario.getEmail(),
				usuario.getSenha());

		if (usuarioOptional.isPresent()) {
			session.setAttribute("email", usuario.getEmail());
			ModelAndView modelAndView = new ModelAndView("index");
			modelAndView.addObject("usuarioLogado", true);
			return modelAndView;
		} else {

			model.addAttribute("error", "Credenciais inválidas");
			return new ModelAndView("login");
		}
	}

	@PostMapping("/cadastrar")
	public ModelAndView cadastrarUsuario(@Valid UsuarioDTO data, HttpServletResponse response) throws IOException {
		if (usuarioRepository.findByEmail(data.email()) != null)
			response.sendRedirect("/falhou");

		Usuario novoUsuario = new Usuario(data.nomeCompleto(), data.nomeUsuario(), data.email(), data.senha(),
				data.dataNascimento());

		this.usuarioRepository.save(novoUsuario);
		ModelAndView modelAndView = new ModelAndView("index"); // Redireciona para a página de login
	    modelAndView.addObject("usuarioLogado", true); // Adiciona um atributo se necessário

	    return modelAndView;
	}

	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}

	@DeleteMapping("/excluir-usuario/{usuarioId}")
	public String excluirDefinitivamente(HttpSession session, @PathVariable Long usuarioId) {

		Optional<Usuario> usuarioOptional = usuarioService.buscarUsuarioPorId(usuarioId);

		if (usuarioOptional.isPresent()) {

			usuarioService.deletarUsuario(usuarioId);

			session.invalidate();

			return "redirect:/index";
		} else {

			return "redirect:/perfil";
		}

	}

	@PostMapping("/alterar-dados")
	public ModelAndView atualizarDadosUsuario(@ModelAttribute("usuario") Usuario usuario, Model model, HttpSession session) {
	    String email = (String) session.getAttribute("email");

	    Usuario usuarioAtual = usuarioRepository.findByEmail(email);
	    
		if (usuario.getNomeCompleto() != null) {
			usuarioAtual.setNomeCompleto(usuario.getNomeCompleto());
		}
		if (usuario.getNomeUsuario() != null) {
			usuarioAtual.setNomeUsuario(usuario.getNomeUsuario());
		}
		if (usuario.getEmail() != null) {
			usuarioAtual.setEmail(usuario.getEmail());
		}
		if (usuario.getDataNascimento() != null) {
		    usuarioAtual.setDataNascimento(usuario.getDataNascimento());
		}
		if (usuario.getSenha() != null) {
			usuarioAtual.setSenha(usuario.getSenha());
		}

		model.addAttribute("usuario", usuario);
		usuarioRepository.save(usuarioAtual);

		return new ModelAndView("redirect:/perfil").addObject("usuarioLogado", true);
	}
}
