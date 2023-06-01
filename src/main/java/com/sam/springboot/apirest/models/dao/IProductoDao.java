package com.sam.springboot.apirest.models.dao;

import com.sam.springboot.apirest.models.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {

    @Query("select p from Producto p where p.nombre like %?1%")
    List<Producto> findByNombre(String nombre);

    //@Query("select p from Producto p where p.nombre like %?1%")
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
