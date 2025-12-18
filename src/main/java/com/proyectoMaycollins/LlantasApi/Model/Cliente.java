package com.proyectoMaycollins.LlantasApi.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builder")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String documentoIdentidad; // DNI, RUC, etc.

    private String telefono;
    private String email;
    private String direccion;

    @Column(name = "activo")
    private Boolean activo = true;
}