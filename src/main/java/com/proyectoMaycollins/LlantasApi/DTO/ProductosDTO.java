package com.proyectoMaycollins.LlantasApi.DTO;

import java.math.BigDecimal;

public class ProductosDTO {
    private Long productosId;
    private String codigoProducto;
    private String descripcion;
    private String imagenUrl;
    private BigDecimal precioVenta;
    private BigDecimal precioMayorista;
    private BigDecimal precioComprado;
    private Boolean activo;
    private String categoria;  // Cambiar a String para recibir el nombre del enum
    private String marca;
    private String modelo;

    public Long getProductosId() { return productosId; }
    public void setProductosId(Long productosId) { this.productosId = productosId; }
    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public BigDecimal getPrecioMayorista() { return precioMayorista; }
    public void setPrecioMayorista(BigDecimal precioMayorista) { this.precioMayorista = precioMayorista; }
    public BigDecimal getPrecioComprado() { return precioComprado; }
    public void setPrecioComprado(BigDecimal precioComprado) { this.precioComprado = precioComprado; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
}
