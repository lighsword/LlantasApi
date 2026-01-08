package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@Table(name = "movimientos_inventario")
public class MovimientosInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long movimientoId;

    @Column(name = "almacen_id", nullable = false)
    private Integer almacenId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "tipo_movimiento", length = 20, nullable = false)
    private String tipoMovimiento;  // ENTRADA, SALIDA, AJUSTE, TRANSFERENCIA

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "cantidad_anterior", nullable = false)
    private Integer cantidadAnterior;

    @Column(name = "cantidad_nueva", nullable = false)
    private Integer cantidadNueva;

    @Column(name = "fecha_movimiento")
    private OffsetDateTime fechaMovimiento;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "referencia_tipo", length = 50)
    private String referenciaTipo;  // COMPRA, VENTA, AJUSTE, TRANSFERENCIA

    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(name = "almacen_destino_id")
    private Integer almacenDestinoId;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", insertable = false, updatable = false)
    private Almacenes almacen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Productos producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_destino_id", insertable = false, updatable = false)
    private Almacenes almacenDestino;

    @PrePersist
    protected void onCreate() {
        if (fechaMovimiento == null) {
            fechaMovimiento = OffsetDateTime.now();
        }
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}

