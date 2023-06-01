package com.sam.springboot.apirest.controller;

import com.sam.springboot.apirest.models.entity.Factura;
import com.sam.springboot.apirest.models.entity.Producto;
import com.sam.springboot.apirest.models.service.IFacturaService;
import com.sam.springboot.apirest.models.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class FacturaRestController {

    @Autowired
    private IFacturaService facturaService;

    @Autowired
    private IProductoService productoService;

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/facturas/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Factura showFactura(@PathVariable Long id){
        return facturaService.findFacturaById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/facturas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        facturaService.deleteFactura(id);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/facturas/filtrar-productos/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public List<Producto> filtrarProductos(@PathVariable String nombre){
        return productoService.findByNombre(nombre);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/facturas")
    @ResponseStatus(HttpStatus.CREATED)
    public Factura crearFactura(@RequestBody Factura factura){
        return facturaService.saveFactura(factura);
    }
}
