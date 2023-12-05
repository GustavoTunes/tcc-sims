package projeto.sims.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import projeto.sims.model.Usuario;

@Controller
public class NavController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/cadastro")
	public ModelAndView exibirFormsCadastro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return new ModelAndView("cadastro");
	}
	
	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = request.getSession();

		session.invalidate();
		return new ModelAndView("index");
	}

	@GetMapping("/login")
	public String exibirFormsLogin() {
		return "login";
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
