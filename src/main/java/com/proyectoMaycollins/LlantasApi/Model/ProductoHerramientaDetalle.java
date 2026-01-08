package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.TipoHerramienta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "producto_herramienta_detalle")
public class ProductoHerramientaDetalle {
    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "producto_id")
    private Productos producto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_uso", length = 255)
    private TipoHerramienta tipoUso;
}

