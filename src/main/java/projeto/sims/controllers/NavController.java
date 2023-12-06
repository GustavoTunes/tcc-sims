package projeto.sims.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.servlet.ModelAndView;
import projeto.sims.model.Usuario;

@Controller
public class NavController {

	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");

		if(email != null) {
			model.addAttribute("usuarioLogado", true);
		}else {
			model.addAttribute("usuarioLogado", false);
		}

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

	@GetMapping("/erro")
	public String erro() {
	return "erro";
	}

	@GetMapping("/termos")
	public String termos(Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");

		if(email != null) {
			model.addAttribute("usuarioLogado", true);
		}else {
			model.addAttribute("usuarioLogado", false);
		}

		return "termos";
	}

	@GetMapping("/cadastroTermos")
	public String cadastroTermos(Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");

		if(email != null) {
			model.addAttribute("usuarioLogado", true);
		}else {
			model.addAttribute("usuarioLogado", false);
		}

		return "cadastroTermos";
	}

	@GetMapping("/privacidade")
	public String privacidade(Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");

		if(email != null) {
			model.addAttribute("usuarioLogado", true);
		}else {
			model.addAttribute("usuarioLogado", false);
		}

		return "privacidade";
	}

	@GetMapping("/sobre")
	public String sobre(Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");

		if(email != null) {
			model.addAttribute("usuarioLogado", true);
		}else {
			model.addAttribute("usuarioLogado", false);
		}

		return "sobre";
	}

	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = request.getSession();

		session.invalidate();
		return new ModelAndView("index");
	}
	
}
