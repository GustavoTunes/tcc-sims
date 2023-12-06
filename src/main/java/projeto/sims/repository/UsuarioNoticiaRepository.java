package projeto.sims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.sims.model.Noticia;
import projeto.sims.model.Usuario;
import projeto.sims.model.UsuarioNoticia;

import java.util.List;
import java.util.Optional;

public interface UsuarioNoticiaRepository extends JpaRepository<UsuarioNoticia, Long> {
   UsuarioNoticia findByUsuarioAndNoticiaUrl(Usuario usuario, String senha);
   List<UsuarioNoticia> findByUsuarioEmail(String email);
   List<UsuarioNoticia> findByUsuario(Usuario usuario);
}
