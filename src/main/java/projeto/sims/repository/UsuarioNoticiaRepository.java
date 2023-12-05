package projeto.sims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.sims.model.Usuario;
import projeto.sims.model.UsuarioNoticia;

import java.util.List;
import java.util.Optional;

public interface UsuarioNoticiaRepository extends JpaRepository<UsuarioNoticia, Long> {
   UsuarioNoticia findByUsuarioEmailAndNoticiaUrl(String email, String senha);
   List<UsuarioNoticia> findByUsuarioEmail(String email);
}
