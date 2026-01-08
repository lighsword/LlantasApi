package com.proyectoMaycollins.LlantasApi.DTO;

import java.time.OffsetDateTime;

public class InventarioDTO {
    private Integer productosId;
    private Integer almacenesId;
    private Integer cantidad;
    private OffsetDateTime fechaActualizacion;

    public Integer getProductosId() { return productosId; }
    public void setProductosId(Integer productosId) { this.productosId = productosId; }
    public Integer getAlmacenesId() { return almacenesId; }
    public void setAlmacenesId(Integer almacenesId) { this.almacenesId = almacenesId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public OffsetDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(OffsetDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}

