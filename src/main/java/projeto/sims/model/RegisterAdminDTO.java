package projeto.sims.model;

import java.sql.Date;

public record RegisterAdminDTO(String email, String senha, UsuarioFuncao funcao, Date dataNascimento, String nomeUsuario, String nomeCompleto) {

}
