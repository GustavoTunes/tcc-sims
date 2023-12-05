package projeto.sims.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projeto.sims.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByEmail(String email);

	Optional<Usuario> findByEmailAndSenha(String email, String senha);
	
}
