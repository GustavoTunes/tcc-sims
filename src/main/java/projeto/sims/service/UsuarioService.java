package projeto.sims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import projeto.sims.model.Usuario;
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

	    public Usuario salvarUsuario(Usuario usuario) {
	        return usuarioRepository.save(usuario);
	    }

	    public void deletarUsuario(Long id) {
	        usuarioRepository.deleteById(id);
	    }
	
}
