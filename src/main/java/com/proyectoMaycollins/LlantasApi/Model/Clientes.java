package com.proyectoMaycollins.LlantasApi.Model;

import com.proyectoMaycollins.LlantasApi.Model.enums.NivelCliente;
import com.proyectoMaycollins.LlantasApi.Model.enums.TipoCliente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "clientes")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long id;

    @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono_cliente")
    private String telefonoCliente;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "documento_identidad")
    private String documentoIdentidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente")
    private TipoCliente tipoCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_cliente")
    private NivelCliente nivelCliente;

    @Column(name = "total_compras")
    private Integer totalCompras;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo;
}

