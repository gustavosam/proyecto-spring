package com.sam.springboot.apirest.models.dao;

import com.sam.springboot.apirest.models.entity.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
}
