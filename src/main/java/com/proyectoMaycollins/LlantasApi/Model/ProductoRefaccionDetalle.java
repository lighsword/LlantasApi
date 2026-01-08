package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "producto_refaccion_detalle")
public class ProductoRefaccionDetalle {
    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "producto_id")
    private Productos producto;

    @Column(name = "numero_parte", length = 100)
    private String numeroParte;

    @Column(name = "sistema_asociado", length = 50)
    private String sistemaAsociado;  // frenos, suspensión, válvulas

    @Column(name = "vehiculo_aplicable", length = 50)
    private String vehiculoAplicable;
}

