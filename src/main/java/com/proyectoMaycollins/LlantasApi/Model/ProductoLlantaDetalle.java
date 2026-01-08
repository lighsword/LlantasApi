package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "producto_llanta_detalle")
public class ProductoLlantaDetalle {
    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "producto_id")
    private Productos producto;

    @Column(name = "medida_llanta", length = 50)
    private String medidaLlanta;  // 205/55R16

    @Column(name = "indice_carga", length = 50)
    private String indiceCarga;

    @Column(name = "indice_velocidad", length = 10)
    private String indiceVelocidad;

    @Column(name = "tipo_terreno", length = 50)
    private String tipoTerreno;  // asfalto, mixto, off-road

    @Column(name = "vehiculo_aplicable", length = 50)
    private String vehiculoAplicable;  // auto, SUV, camión
}

