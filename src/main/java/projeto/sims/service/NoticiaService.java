package projeto.sims.service;

import org.springframework.stereotype.Service;
import projeto.sims.model.Noticia;
import projeto.sims.repository.NoticiaRepository;

@Service
public class NoticiaService {

    private final NoticiaRepository noticiaRepository;

    public NoticiaService(NoticiaRepository noticiaRepository) {
        this.noticiaRepository = noticiaRepository;
    }

    public void salvarNoticia(Noticia noticia) {
        noticiaRepository.save(noticia);
    }

    public void deletarNoticia(Noticia noticia) {
        noticiaRepository.delete(noticia);
    }
}
