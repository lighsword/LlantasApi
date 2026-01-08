package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;

@Data
public class ProductoRefaccionDetalleDTO {
    private Long productoId;
    private String numeroParte;
    private String sistemaAsociado;     // frenos, suspensión, válvulas
    private String vehiculoAplicable;
}

