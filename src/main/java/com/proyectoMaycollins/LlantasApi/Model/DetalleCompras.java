package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "detalle_compras")
@IdClass(DetalleCompras.DetalleComprasId.class)
public class DetalleCompras {

    @Id
    @Column(name = "compra_id")
    private Integer compraId;

    @Id
    @Column(name = "producto_id")
    private Long productoId;

    private Integer cantidad;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "compra_id", insertable = false, updatable = false)
    private Compras compra;

    @ManyToOne
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Productos producto;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class DetalleComprasId implements Serializable {
        private Integer compraId;
        private Long productoId;
    }
}

