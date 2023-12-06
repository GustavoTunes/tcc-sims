package projeto.sims.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.sims.model.Noticia;
import projeto.sims.model.Usuario;
import projeto.sims.model.UsuarioNoticia;
import projeto.sims.repository.UsuarioNoticiaRepository;

import java.util.List;

@Service
public class UsuarioNoticiaService {
    private final UsuarioService usuarioService;
    private final NoticiaService noticiaService;
    private final UsuarioNoticiaRepository usuarioNoticiaRepository;


    public UsuarioNoticiaService(UsuarioService usuarioService, NoticiaService noticiaService, UsuarioNoticiaRepository usuarioNoticiaRepository) {
        this.usuarioService = usuarioService;
        this.noticiaService = noticiaService;
        this.usuarioNoticiaRepository = usuarioNoticiaRepository;
    }

    public void salvarUsuarioNoticia(UsuarioNoticia usuarioNoticia) {
        usuarioNoticiaRepository.save(usuarioNoticia);
    }

    public UsuarioNoticia buscarPeloUsuarioEmailENoticiaUrl(Usuario usuario, String url) {
        return usuarioNoticiaRepository.findByUsuarioAndNoticiaUrl(usuario, url);
    }

    public List<UsuarioNoticia> buscarPeloUsuarioEmail(String email) {
       return usuarioNoticiaRepository.findByUsuarioEmail(email);
    }

    public List<UsuarioNoticia> buscarPeloUsuario(Usuario usuario) {
        return usuarioNoticiaRepository.findByUsuario(usuario);
    }

    @Transactional
    public void associarUsuarioNoticia(Usuario usuario, Noticia noticia) {
        noticiaService.salvarNoticia(noticia);

        UsuarioNoticia usuarioNoticia = new UsuarioNoticia(usuario, noticia, noticia.getUrlNoticia(), usuario.getEmail());
        salvarUsuarioNoticia(usuarioNoticia);
        usuario.getUsuarioNoticias().add(usuarioNoticia);
        noticia.getUsuarioNoticias().add(usuarioNoticia);

        usuarioService.salvarUsuario(usuario);
        noticiaService.salvarNoticia(noticia);
    }

    @Transactional
    public void desassociarNoticiaUsuario(Usuario usuario, Noticia noticia) {
        UsuarioNoticia usuarioNoticia = buscarPeloUsuarioEmailENoticiaUrl(usuario, noticia.getUrlNoticia());

        if (usuarioNoticia != null) {

            usuario.getUsuarioNoticias().remove(usuarioNoticia);
            noticia.getUsuarioNoticias().remove(usuarioNoticia);

            if (noticia.getUsuarioNoticias().isEmpty()) {
                noticiaService.deletarNoticia(noticia);
            }

            usuarioNoticiaRepository.delete(usuarioNoticia);

            usuarioService.salvarUsuario(usuario);
        }
    }
}
