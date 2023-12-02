package projeto.sims.model;

import java.sql.Date;

public record RegisterDTO(String email, String senha, Date dataNascimento, String nomeUsuario, String nomeCompleto) {

}
