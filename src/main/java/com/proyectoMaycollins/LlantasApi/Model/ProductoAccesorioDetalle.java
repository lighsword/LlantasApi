package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "producto_accesorio_detalle")
public class ProductoAccesorioDetalle {
    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "producto_id")
    private Productos producto;

    @Column(name = "tipo_accesorio", length = 50)
    private String tipoAccesorio;  // tapacubo, cadena

    @Column(name = "material", length = 50)
    private String material;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "diametro_aplicable", precision = 5, scale = 2)
    private BigDecimal diametroAplicable;

    @Column(name = "vehiculo_aplicable", length = 50)
    private String vehiculoAplicable;

    @Column(name = "presentacion", length = 30)
    private String presentacion;  // unidad, par, juego
}

