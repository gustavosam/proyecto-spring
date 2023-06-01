package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.dao.IRegionDao;
import com.sam.springboot.apirest.models.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements IRegionService{

    @Autowired
    private IRegionDao regionDao;

    @Override
    public List<Region> findAll() {
        return regionDao.findAll();
    }
}
