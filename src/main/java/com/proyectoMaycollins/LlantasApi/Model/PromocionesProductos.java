package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "promociones_productos")
@IdClass(PromocionesProductos.PromocionesProductosId.class)
public class PromocionesProductos {

    @Id
    @Column(name = "promocion_id")
    private Long promocionId;

    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @ManyToOne
    @JoinColumn(name = "promocion_id", insertable = false, updatable = false)
    private Promociones promocion;

    @ManyToOne
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Productos producto;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PromocionesProductosId implements Serializable {
        private Long promocionId;
        private Long productoId;
    }
}

