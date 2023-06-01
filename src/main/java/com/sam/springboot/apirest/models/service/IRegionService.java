package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.entity.Region;
import java.util.List;
public interface IRegionService {

    List<Region> findAll();
}
