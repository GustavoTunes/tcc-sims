package projeto.sims.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import projeto.sims.model.Usuario;

@Controller
public class NavController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/cadastro")
	public String exibirFormsCadastro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "cadastro";
	}

	@GetMapping("/login")
	public String exibirFormsLogin() {
		return "login";
	}

	@GetMapping("/perfil")
	public String perfil() {
		return "perfil";
	}

	@GetMapping("/indexLogoff")
	public String indexLogoff() {
		return "indexLogoff";
	}

	@GetMapping("/erro")
	public String erro() {
	return "erro";
	}

	@GetMapping("/termos")
	public String termos() {
	return "termos";
	}

	@GetMapping("/cadastroTermos")
	public String cadastroTermos() {
	return "cadastroTermos";
	}

	@GetMapping("/privacidade")
	public String privacidade() {
	return "privacidade";
	}

	@GetMapping("/sobre")
	public String sobre() {
	return "sobre";
	
	}
	
}
