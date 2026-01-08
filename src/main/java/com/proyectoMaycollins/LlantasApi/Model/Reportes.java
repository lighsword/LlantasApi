package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reportes")
public class Reportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reporte_id")
    private Long id;

    @Column(name = "nombre_reporte", nullable = false)
    private String nombreReporte;

    @Column(name = "tipo_reporte", nullable = false)
    private String tipoReporte;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "fecha_puntual")
    private LocalDateTime fechaPuntual;

    @Column(name = "fecha_generacion", insertable = false, updatable = false)
    private LocalDateTime fechaGeneracion;

    @ManyToOne
    @JoinColumn(name = "generado_por", nullable = false)
    private Usuarios generadoPor;

    @Column(name = "titulo_impreso")
    private String tituloImpreso;

    // Los JSONB los mapeamos como String por simplicidad en JPA básico
    private String resumen;

    @Column(nullable = false)
    private String detalle;

    @Column(name = "version_datos")
    private String versionDatos;

    @Column(name = "formato_salida")
    private String formatoSalida;

    private Boolean firmado;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedores proveedor;

    @ManyToOne
    @JoinColumn(name = "almacen_id")
    private Almacenes almacen;
}

