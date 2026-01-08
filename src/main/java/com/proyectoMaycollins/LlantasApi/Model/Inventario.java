package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@Table(name = "inventario")
@IdClass(Inventario.InventarioId.class)
public class Inventario {

    @Id
    @Column(name = "producto_id")
    private Integer productoId;

    @Id
    @Column(name = "almacen_id")
    private Integer almacenId;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class InventarioId implements Serializable {
        private Integer productoId;
        private Integer almacenId;
    }
}

