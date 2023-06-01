package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.entity.Usuario;

public interface IUsuarioService {

    Usuario findByUsername(String username);
}
