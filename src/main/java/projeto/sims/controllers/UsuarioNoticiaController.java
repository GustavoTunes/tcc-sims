package projeto.sims.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import projeto.sims.model.Noticia;
import projeto.sims.model.Usuario;
import projeto.sims.model.UsuarioNoticia;
import projeto.sims.repository.UsuarioRepository;
import projeto.sims.service.UsuarioNoticiaService;
import projeto.sims.service.UsuarioService;

import java.util.List;

@RestController
public class UsuarioNoticiaController {

	private final UsuarioService usuarioService;
    private final UsuarioNoticiaService usuarioNoticiaService;

    public UsuarioNoticiaController(UsuarioNoticiaService usuarioNoticiaService, UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    	this.usuarioNoticiaService = usuarioNoticiaService;
    }

    @GetMapping("/meu-perfil")
    public ModelAndView listarNoticiasFavoritas(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");

        List<UsuarioNoticia> listaUsuarioNoticias = usuarioNoticiaService.buscarPeloUsuarioEmail(email);
        List<Noticia> noticiasList = listaUsuarioNoticias.stream()
                .map(UsuarioNoticia::getNoticia)
                .toList();
        
        System.out.println(noticiasList);

        Usuario usuario = usuarioService.buscarPorEmail(email);
        model.addAttribute("usuario", usuario);
        model.addAttribute("noticiasList", noticiasList);

        return new ModelAndView("perfil").addObject("usuarioLogado", true);
    }
    
}
