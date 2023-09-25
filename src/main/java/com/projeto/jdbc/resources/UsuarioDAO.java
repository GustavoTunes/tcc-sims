package com.projeto.jdbc.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.projeto.jdbc.model.Usuario;
import com.projeto.jdbc.service.ConnectionFactory;

public class UsuarioDAO {

    private static final String INSERIR_USUARIO_SQL = "INSERT INTO usuarios (nome_completo, nome_usuario, email, senha, data_nascimento) VALUES (?, ?, ?, ?, ?)";
    private static final String OBTER_TODOS_USUARIOS_SQL = "SELECT * FROM usuarios";

    public boolean inserirUsuario(Usuario usuario) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERIR_USUARIO_SQL)) {

            preparedStatement.setString(1, usuario.getNomeCompleto());
            preparedStatement.setString(2, usuario.getNomeUsuario());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getSenha());
            preparedStatement.setDate(5, new java.sql.Date(usuario.getDataNascimento().getTime()));

            int linhasAfetadas = preparedStatement.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Usuario> obterTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OBTER_TODOS_USUARIOS_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }
    
    public Usuario obterUsuarioPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return criarUsuario(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome_completo = ?, nome_usuario = ?, email = ?, senha = ?, data_nascimento = ? WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNomeCompleto());
            preparedStatement.setString(2, usuario.getNomeUsuario());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getSenha());
            preparedStatement.setDate(5, new java.sql.Date(usuario.getDataNascimento().getTime()));
            preparedStatement.setInt(6, usuario.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Usuario criarUsuario(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getInt("id"));
        usuario.setNomeCompleto(resultSet.getString("nome_completo"));
        usuario.setNomeUsuario(resultSet.getString("nome_usuario"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setDataNascimento(resultSet.getDate("data_nascimento"));
        return usuario;
    }
}
