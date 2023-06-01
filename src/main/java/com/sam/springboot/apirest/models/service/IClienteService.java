package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.entity.Cliente;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IClienteService {

    List<Cliente> findAll();

    Page<Cliente> findAll(Pageable pageable);

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> save(Cliente cliente, BindingResult bindingResult);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> update(Cliente cliente, BindingResult bindingResult, Long id);

    ResponseEntity<?> upload(MultipartFile archivo, Long id);

    ResponseEntity<Resource> verFoto(String nombreFoto);
    boolean existsById(Long id);

}
