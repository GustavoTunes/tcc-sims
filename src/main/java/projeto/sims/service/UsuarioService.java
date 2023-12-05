package projeto.sims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import projeto.sims.model.Noticia;
import projeto.sims.model.Usuario;
import projeto.sims.model.UsuarioNoticia;
import projeto.sims.repository.NoticiaRepository;
import projeto.sims.repository.UsuarioNoticiaRepository;
import projeto.sims.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}
	
	 public List<Usuario> listarTodosUsuarios() {
	        return usuarioRepository.findAll();
	    }

	public Optional<Usuario> buscarUsuarioPorId(Long id) {
		return usuarioRepository.findById(id);
	}

	public void salvarUsuario(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	public void deletarUsuario(Long id) {
	        usuarioRepository.deleteById(id);
	    }

	public Optional<Usuario> buscarUsuarioPorEmailESenha(String email, String senha) {
		return usuarioRepository.findByEmailAndSenha(email, senha);
	}

	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
}
