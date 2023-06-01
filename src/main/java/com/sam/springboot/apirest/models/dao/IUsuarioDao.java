package com.sam.springboot.apirest.models.dao;

import com.sam.springboot.apirest.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

}
