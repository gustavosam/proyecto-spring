package com.sam.springboot.apirest.models.dao;

import com.sam.springboot.apirest.models.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRegionDao extends JpaRepository<Region, Long> {
}
