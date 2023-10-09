package com.projeto.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.banco.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
