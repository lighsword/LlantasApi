package com.proyectoMaycollins.LlantasApi.DTO;

import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoVenta;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VentasDTO {
    private Long id;
    private Long clientesId;
    private Long usuariosId;
    private LocalDateTime fechaVenta;
    private Double total;
    private Double descuentoAplicado;
    private EstadoVenta estado;
    private String serieComprobante;
    private String numeroComprobante;
}

