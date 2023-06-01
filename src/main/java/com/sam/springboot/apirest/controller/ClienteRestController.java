package com.sam.springboot.apirest.controller;

import com.sam.springboot.apirest.models.entity.Cliente;
import com.sam.springboot.apirest.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class ClienteRestController{

    @Autowired
    private IClienteService clienteService;

    /*@GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> index(){
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }*/

    @GetMapping("/clientes")
    public ResponseEntity<Page<Cliente>> index(@RequestParam(defaultValue = "0", name = "page") Integer page){
        Pageable pageable = PageRequest.of(page,5);
        return new ResponseEntity<>(clienteService.findAll(pageable), HttpStatus.OK);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return clienteService.findById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/clientes")
    public ResponseEntity<?> save(@Valid @RequestBody Cliente cliente, BindingResult bindingResult){
        return clienteService.save(cliente, bindingResult);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult bindingResult, @PathVariable("id") Long id ){
        return clienteService.update(cliente, bindingResult, id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        return clienteService.delete(id);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
        return clienteService.upload(archivo,id);
    }

    @GetMapping("/uploads/img/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
        return clienteService.verFoto(nombreFoto);
    }
}
