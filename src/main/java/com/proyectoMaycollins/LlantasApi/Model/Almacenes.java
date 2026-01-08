package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "almacenes")
public class Almacenes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "almacen_id")
    private Integer almacenId;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "capacidad_maxima", precision = 10, scale = 2)
    private BigDecimal capacidadMaxima;

    @Column(name = "activo")
    private Boolean activo;
}
