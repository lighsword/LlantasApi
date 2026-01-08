package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.CategoriaNombre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "productos")
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "codigo_producto", length = 255)
    private String codigoProducto;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Column(name = "precio_venta", precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "precio_mayorista", precision = 10, scale = 2)
    private BigDecimal precioMayorista;

    @Column(name = "precio_comprado", precision = 10, scale = 2)
    private BigDecimal precioComprado;

    @Column(name = "activo")
    private Boolean activo;

    // Usar Enum CategoriaNombre en lugar de categoriaId
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", length = 50)
    private CategoriaNombre categoria;

    @Column(name = "marca", length = 255)
    private String marca;

    @Column(name = "modelo", length = 255)
    private String modelo;
}
