package com.sam.springboot.apirest.models.service;

import com.sam.springboot.apirest.models.entity.Factura;

public interface IFacturaService {

    Factura findFacturaById(Long id);

    Factura saveFactura(Factura factura);

    void deleteFactura(Long id);
}
