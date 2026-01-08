package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.TipoPrecio;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "precios", indexes = {
    @Index(name = "idx_precios_producto", columnList = "producto_id"),
    @Index(name = "idx_precios_tipo_activo", columnList = "tipo, activo")
})
public class Precios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "precio_id")
    private Long precioId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 15, nullable = false)
    private TipoPrecio tipo;  // COMPRA, VENTA, MAYORISTA

    @Column(name = "precio", precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "fecha_inicio", nullable = false)
    private OffsetDateTime fechaInicio;

    @Column(name = "activo")
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Productos producto;

    @PrePersist
    protected void onCreate() {
        if (fechaInicio == null) {
            fechaInicio = OffsetDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }
}

