package projeto.sims.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.servlet.ModelAndView;
import projeto.sims.model.UsuarioDTO;
import projeto.sims.model.Usuario;
import projeto.sims.service.UsuarioService;

@Controller
public class UsuarioController {
	
	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}


	@PostMapping("/home")
	public ModelAndView entrar(@ModelAttribute("usuario") Usuario usuario, Model model, HttpSession session) {

		Optional<Usuario> usuarioOptional = usuarioService.buscarUsuarioPorEmailESenha(usuario.getEmail(), usuario.getSenha());

		if (usuarioOptional.isPresent()) {
			session.setAttribute("email", usuario.getEmail());
			ModelAndView modelAndView = new ModelAndView("index");
			modelAndView.addObject("usuarioLogado", true);
			return modelAndView;
		} else {
			// Autenticação falhou
			model.addAttribute("error", "Credenciais inválidas");
			return new ModelAndView("login");
		}
	}

	@PostMapping("/cadastrar")
	public ModelAndView cadastrarUsuario(@Valid UsuarioDTO data, HttpSession session, HttpServletResponse response) throws IOException {
		ModelAndView modelAndView = new ModelAndView("index"); // Inicializa o ModelAndView

		if (usuarioService.buscarPorEmail(data.email()) != null) {
			response.sendRedirect("/cadastro");
		} else {
			Usuario novoUsuario = new Usuario(data.nomeCompleto(), data.nomeUsuario(), data.email(), data.senha(),
					data.dataNascimento());
			session.setAttribute("email", novoUsuario.getEmail());

			this.usuarioService.salvarUsuario(novoUsuario);
			modelAndView.addObject("usuarioLogado", true); // Adiciona um atributo se necessário
		}

		return modelAndView;
	}

	@DeleteMapping("/excluir-usuario/{usuarioId}")
	public String excluirDefinitivamente(@PathVariable Long usuarioId, HttpSession session) {

		Optional<Usuario> usuarioOptional = usuarioService.buscarUsuarioPorId(usuarioId);

		if (usuarioOptional.isPresent()) {

			usuarioService.deletarUsuario(usuarioId);

			session.invalidate();
			return "login";
		} else {
			return "redirect:/perfil";
		}
	}

	@Transactional
	@PostMapping("/alterar-usuario")
	public ModelAndView alterarDados(@RequestBody UsuarioDTO data, HttpSession session, Model model) {
		String email = (String) session.getAttribute("email");
		Usuario usuario = usuarioService.buscarPorEmail(email);

		// Lógica para alterar os dados do usuário
		if(!Objects.equals(email, data.email())) {
			email = data.email();
			session.setAttribute("email", email);
		}

		usuario.setNomeUsuario(data.nomeUsuario());
		usuario.setNomeCompleto(data.nomeCompleto());
		usuario.setDataNascimento(data.dataNascimento());
		usuario.setEmail(data.email());
		usuario.setSenha(data.senha());
		

		ModelAndView modelAndView = new ModelAndView("perfil");
		modelAndView.addObject("usuario", usuario);
		model.addAttribute("usuario",usuario);
		usuarioService.salvarUsuario(usuario);
		return new ModelAndView("perfil");
	}
}


