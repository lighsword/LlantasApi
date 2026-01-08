package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

@Data
public class ProductoLlantaDetalleDTO {
    private Long productoId;
    private String medidaLlanta;        // 205/55R16
    private String indiceCarga;
    private String indiceVelocidad;
    private String tipoTerreno;         // asfalto, mixto, off-road
    private String vehiculoAplicable;   // auto, SUV, camión
}
