package com.proyectoMaycollins.LlantasApi.DTO;

import lombok.Data;
import java.util.List;

@Data
public class VentaCompletaDTO {
    private Long clienteId;
    private Long usuarioId;
    private String metodoPago;
    private Double efectivo;
    private Double vuelto;
    private String tipoComprobante;
    private String serieComprobante;
    private String numeroComprobante;
    private Double descuentoAplicado;
    private Double total;
    private List<DetalleVentaItemDTO> detalles;
}
