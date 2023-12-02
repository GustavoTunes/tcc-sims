package com.projeto.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.projeto.banco.model.Usuario;

@Component
@Repository
public interface UsuarioRepository extends 
JpaRepository<Usuario, Long>{

}
