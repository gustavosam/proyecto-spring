package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.dao.IClienteDao;
import com.sam.springboot.apirest.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@ControllerAdvice
public class ClienteServiceImpl implements IClienteService{

    @Autowired
    private IClienteDao clienteDao;


    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {

        Cliente cliente = null;
        Map<String, Object> response =new HashMap<>();
        try {
            cliente = clienteDao.findById(id).get();
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (NoSuchElementException e){
            response.put("mensaje", "El cliente con el ID: ".concat(id.toString()).concat(" No existe"));
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleError405(Model model, Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error en el tipo de dato del id del alumno");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<?> save(Cliente cliente, BindingResult bindingResult) {

        Cliente clienteNew = null;
        Map<String, Object> response = new HashMap<>();

        if(bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(error -> response.put(error.getField().toString(), error.getDefaultMessage()));

            /*List<String> errores = bindingResult.getFieldErrors().stream().map(error -> "El campo "+error.getField()+" "+ error.getDefaultMessage() ).collect(Collectors.toList());
            response.put("errors", errores);*/
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            clienteNew = clienteDao.save(cliente);
        } catch (DataIntegrityViolationException   e){
            response.put("mensaje", "El usuario ya existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el registro");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El cliente fue creado con exito desde el backend");
        response.put("cliente", clienteNew);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {

        ResponseEntity<?> clienteOld = findById(id);
        Map<String, Object> response = new HashMap<>();

        if(clienteOld.getStatusCode().is2xxSuccessful()){
            try {
                eliminarFotoAnteriorDelCliente( (Cliente) clienteOld.getBody());

                clienteDao.deleteById(id);
            }catch (DataAccessException e){
                response.put("mensaje", "Error al intentar eliminar el registro");
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.put("mensaje", "El cliente se eliminó correctamente desde el backend");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return clienteOld;

    }

    @Override
    public ResponseEntity<?> update(Cliente cliente, BindingResult bindingResult, Long id) {

        if(bindingResult.hasErrors()){
            Map<String, Object> response = new HashMap<>();

            bindingResult.getFieldErrors().forEach(error -> response.put(error.getField().toString(), error.getDefaultMessage()));
            /*List<String> errores = bindingResult.getFieldErrors().stream().map(error -> "El campo "+error.getField()+" "+ error.getDefaultMessage() ).collect(Collectors.toList());
            response.put("errors", errores);*/
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<?> cliente0 = findById(id);

        if (cliente0.getStatusCode().is2xxSuccessful()){
            Cliente cliente1 = (Cliente) cliente0.getBody();
            cliente1.setNombre(cliente.getNombre());
            cliente1.setApellido(cliente.getApellido());
            cliente1.setEmail(cliente.getEmail());
            cliente1.setFechaNacimiento(cliente.getFechaNacimiento());
            cliente1.setRegion(cliente.getRegion());

            Map<String, Object> response = new HashMap<>();

            Cliente clienteUpdated= null;
            try {
                 clienteUpdated = clienteDao.save(cliente1);
            }catch (DataAccessException e){
                response.put("mensaje", "Error al actualizar el registro");
                response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("mensaje","El cliente fue actualizado con exito desde el backend");
            response.put("cliente", clienteUpdated);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }

        return cliente0;

    }

    @Override
    public ResponseEntity<?> upload(MultipartFile archivo, Long id) {
        Map<String, Object> response = new HashMap<>();
        ResponseEntity<?> cliente0 = findById(id);

        if(cliente0.getStatusCode().is5xxServerError() || cliente0.getStatusCode().is4xxClientError()){
            return cliente0;
        }
        Cliente oldCliente = (Cliente) cliente0.getBody();

        if(!archivo.isEmpty()){
            String nombreArchivo = UUID.randomUUID().toString()+"_"+ archivo.getOriginalFilename().replace(" ","");
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

            try {
                Files.copy(archivo.getInputStream(), rutaArchivo);
            } catch (IOException e) {
                response.put("mensaje", "Error al subir la imagen");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            eliminarFotoAnteriorDelCliente(oldCliente);

            oldCliente.setFoto(nombreArchivo);
            Cliente newCliente = clienteDao.save(oldCliente);
            response.put("cliente", newCliente);
            response.put("mensaje", "Se ha subido la imagen con éxito !!! ");
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Resource> verFoto(String nombreFoto) {
        Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
        System.out.println(rutaArchivo.toString());
        Resource recurso = null;

        try {
            recurso = new UrlResource(rutaArchivo.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(!recurso.exists() || !recurso.isReadable()){
            rutaArchivo = Paths.get("src/main/resources/static/img").resolve("not-user.jpg").toAbsolutePath();

            try {
                recurso = new UrlResource(rutaArchivo.toUri());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpHeaders cabecera = new HttpHeaders();
            cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"");

            return new ResponseEntity<>(recurso, cabecera ,HttpStatus.OK);
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"");

        return new ResponseEntity<>(recurso, cabecera ,HttpStatus.OK);
    }




    private void eliminarFotoAnteriorDelCliente(Cliente cliente){
        String fotoAnterior = cliente.getFoto();

        if(fotoAnterior != null && fotoAnterior.length()>0){
            Path rutaFotoAnterior = Paths.get("uploads").resolve(fotoAnterior).toAbsolutePath();
            File archivoAnterior = rutaFotoAnterior.toFile();

            if(archivoAnterior.exists() && archivoAnterior.canRead()){
                archivoAnterior.delete();
            }
        }
    }

    @Override
    public boolean existsById(Long id) {
        return clienteDao.existsById(id);
    }

}
