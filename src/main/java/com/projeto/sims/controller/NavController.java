package com.projeto.sims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.projeto.banco.model.Usuario;

@Controller
public class NavController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/cadastro")
	public String cadastro(Model model) {
		model.addAttribute("usuarioForm", new Usuario());
		return "cadastro";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/categorias")
	public String categorias() {
		return "categoria";
	}
	
}
