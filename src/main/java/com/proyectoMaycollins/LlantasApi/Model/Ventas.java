package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoVenta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ventas")
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Clientes cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta; // sin default en DB

    private Double total;

    @Column(name = "descuento_aplicado")
    private Double descuentoAplicado;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    @Column(name = "serie_comprobante")
    private String serieComprobante;

    @Column(name = "numero_comprobante")
    private String numeroComprobante;
}
