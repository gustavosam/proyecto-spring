package com.sam.springboot.apirest.controller;

import com.sam.springboot.apirest.models.entity.Region;
import com.sam.springboot.apirest.models.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class RegionRestController {

    @Autowired
    private IRegionService regionService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/regiones")
    public ResponseEntity<List<Region>> index(){
        return new ResponseEntity<>(regionService.findAll(), HttpStatus.OK);
    }
}
