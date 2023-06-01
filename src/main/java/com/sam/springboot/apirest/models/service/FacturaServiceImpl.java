package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.dao.IFacturaDao;
import com.sam.springboot.apirest.models.entity.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaServiceImpl implements IFacturaService{

    @Autowired
    private IFacturaDao facturaDao;

    @Override
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    public Factura saveFactura(Factura factura) {
        return facturaDao.save(factura);
    }

    @Override
    public void deleteFactura(Long id) {
        facturaDao.deleteById(id);
    }
}
