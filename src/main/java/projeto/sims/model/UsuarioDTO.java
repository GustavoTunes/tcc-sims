package projeto.sims.model;

import java.sql.Date;

public record UsuarioDTO(String email, String senha, Date dataNascimento, String nomeUsuario, String nomeCompleto) {

}
