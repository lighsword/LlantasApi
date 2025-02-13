package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoriaId;

    @Column(nullable = false)
    private String nombreCategoria;

    private String modelo;
    private String medida;
    private String especificaciones;
    private String tipoRefaccion;
    private String compatibilidad;

    @Column(name = "verificar_disponibilidad")
    private Boolean verificarDisponibilidad;
}