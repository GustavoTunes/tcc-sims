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

    private final UsuarioRepository usuarioRepository;
    private final UsuarioNoticiaService usuarioNoticiaService;
    private final UsuarioService usuarioService;

    public UsuarioNoticiaController(UsuarioNoticiaService usuarioNoticiaService, UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioNoticiaService = usuarioNoticiaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/meu-perfil")
    public ModelAndView listarNoticiasFavoritas(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Usuario usuario = usuarioService.buscarPorEmail(email);

        List<UsuarioNoticia> listaUsuarioNoticias = usuarioNoticiaService.buscarPeloUsuario(usuario);
        List<Noticia> noticiasList = listaUsuarioNoticias.stream()
                .map(UsuarioNoticia::getNoticia)
                .toList();

        model.addAttribute("usuario", usuario);
        model.addAttribute("noticiasList", noticiasList);

        return new ModelAndView("perfil").addObject("usuarioLogado", true);
    }
}
