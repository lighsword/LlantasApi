package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "categoria_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoriaId;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    
    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;
}