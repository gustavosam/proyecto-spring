package com.sam.springboot.apirest.models.dao;

import com.sam.springboot.apirest.models.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
}
