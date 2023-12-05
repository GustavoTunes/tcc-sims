package projeto.sims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.sims.model.Noticia;
import projeto.sims.model.Usuario;

import java.util.Optional;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    Noticia findByUrlNoticia(String urlNoticia);

}
