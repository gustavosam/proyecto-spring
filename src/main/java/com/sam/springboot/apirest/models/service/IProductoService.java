package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.entity.Producto;

import java.util.List;

public interface IProductoService {

    List<Producto> findByNombre(String nombre);
}
