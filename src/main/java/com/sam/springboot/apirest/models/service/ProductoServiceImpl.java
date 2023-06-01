package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.dao.IProductoDao;
import com.sam.springboot.apirest.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements IProductoService{

    @Autowired
    private IProductoDao productoDao;

    @Override
    public List<Producto> findByNombre(String nombre) {
        return productoDao.findByNombreContainingIgnoreCase(nombre);
    }
}
