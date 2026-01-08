package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "producto_insumo_detalle")
public class ProductoInsumoDetalle {
    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "producto_id")
    private Productos producto;

    @Column(name = "tipo_insumo", length = 50)
    private String tipoInsumo;  // lubricante, sellador

    @Column(name = "contenido_neto", precision = 10, scale = 2)
    private BigDecimal contenidoNeto;

    @Column(name = "unidad_medida", length = 10)
    private String unidadMedida;  // ml, L, g

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "uso_recomendado", length = 50)
    private String usoRecomendado;  // montaje, reparación
}

