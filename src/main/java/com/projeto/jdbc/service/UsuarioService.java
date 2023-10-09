package com.projeto.jdbc.service;

import java.util.List;

import com.projeto.jdbc.model.Usuario;
import com.projeto.jdbc.resources.UsuarioDAO;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean inserirUsuario(Usuario usuario) {
        return usuarioDAO.inserirUsuario(usuario);
    }

    public List<Usuario> obterTodosUsuarios() {
        return usuarioDAO.obterTodosUsuarios();
    }

    public Usuario obterUsuarioPorId(int id) {
        return usuarioDAO.obterUsuarioPorId(id);
    }

    public boolean atualizarUsuario(Usuario usuario) {
        return usuarioDAO.atualizarUsuario(usuario);
    }

    public boolean excluirUsuario(int id) {
        return usuarioDAO.excluirUsuario(id);
    }
}
