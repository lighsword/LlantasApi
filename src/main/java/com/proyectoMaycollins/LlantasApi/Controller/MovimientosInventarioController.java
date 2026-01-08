package com.proyectoMaycollins.LlantasApi.Controller;

import com.proyectoMaycollins.LlantasApi.DTO.MovimientosInventarioDTO;
import com.proyectoMaycollins.LlantasApi.DTO.RegistrarMovimientoDTO;
import com.proyectoMaycollins.LlantasApi.Service.MovimientosInventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos-inventario")
@Tag(name = "📦 Movimientos de Inventario", description = "Gestión de movimientos de entrada/salida de inventario")
@SecurityRequirement(name = "Bearer Authentication")
public class MovimientosInventarioController {

    private final MovimientosInventarioService movimientosService;

    public MovimientosInventarioController(MovimientosInventarioService movimientosService) {
        this.movimientosService = movimientosService;
    }

    @Operation(
            summary = "Registrar movimiento de inventario",
            description = "Registra una entrada, salida, ajuste o transferencia de inventario"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping
    public ResponseEntity<MovimientosInventarioDTO> registrarMovimiento(
            @Valid @RequestBody RegistrarMovimientoDTO movimientoDTO) {
        try {
            MovimientosInventarioDTO movimiento = movimientosService.registrarMovimiento(movimientoDTO);
            return ResponseEntity
                    .created(URI.create("/api/movimientos-inventario/" + movimiento.getMovimientoId()))
                    .body(movimiento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener movimiento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MovimientosInventarioDTO> getMovimientoById(
            @Parameter(description = "ID del movimiento") @PathVariable Long id) {
        MovimientosInventarioDTO movimiento = movimientosService.getMovimientoById(id);
        return movimiento != null ? ResponseEntity.ok(movimiento) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar movimientos por almacén")
    @GetMapping("/almacen/{almacenId}")
    public ResponseEntity<List<MovimientosInventarioDTO>> getMovimientosByAlmacen(
            @Parameter(description = "ID del almacén") @PathVariable Integer almacenId) {
        return ResponseEntity.ok(movimientosService.getMovimientosByAlmacen(almacenId));
    }

    @Operation(summary = "Listar movimientos por producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<MovimientosInventarioDTO>> getMovimientosByProducto(
            @Parameter(description = "ID del producto") @PathVariable Long productoId) {
        return ResponseEntity.ok(movimientosService.getMovimientosByProducto(productoId));
    }

    @Operation(summary = "Listar movimientos por rango de fechas")
    @GetMapping("/fechas")
    public ResponseEntity<List<MovimientosInventarioDTO>> getMovimientosByFechas(
            @Parameter(description = "Fecha inicio (ISO 8601)", example = "2026-01-01T00:00:00Z")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fechaInicio,
            @Parameter(description = "Fecha fin (ISO 8601)", example = "2026-01-31T23:59:59Z")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fechaFin) {
        return ResponseEntity.ok(movimientosService.getMovimientosByFechas(fechaInicio, fechaFin));
    }

    @Operation(
            summary = "Registrar entrada rápida",
            description = "Atajo para registrar una entrada de inventario"
    )
    @PostMapping("/entrada")
    public ResponseEntity<MovimientosInventarioDTO> registrarEntrada(
            @Valid @RequestBody RegistrarMovimientoDTO movimientoDTO) {
        movimientoDTO.setTipoMovimiento("ENTRADA");
        return registrarMovimiento(movimientoDTO);
    }

    @Operation(
            summary = "Registrar salida rápida",
            description = "Atajo para registrar una salida de inventario"
    )
    @PostMapping("/salida")
    public ResponseEntity<MovimientosInventarioDTO> registrarSalida(
            @Valid @RequestBody RegistrarMovimientoDTO movimientoDTO) {
        movimientoDTO.setTipoMovimiento("SALIDA");
        return registrarMovimiento(movimientoDTO);
    }

    @Operation(
            summary = "Registrar ajuste de inventario",
            description = "Ajustar el stock a una cantidad específica"
    )
    @PostMapping("/ajuste")
    public ResponseEntity<MovimientosInventarioDTO> registrarAjuste(
            @Valid @RequestBody RegistrarMovimientoDTO movimientoDTO) {
        movimientoDTO.setTipoMovimiento("AJUSTE");
        return registrarMovimiento(movimientoDTO);
    }

    @Operation(
            summary = "Registrar transferencia entre almacenes",
            description = "Transferir productos de un almacén a otro"
    )
    @PostMapping("/transferencia")
    public ResponseEntity<MovimientosInventarioDTO> registrarTransferencia(
            @Valid @RequestBody RegistrarMovimientoDTO movimientoDTO) {
        if (movimientoDTO.getAlmacenDestinoId() == null) {
            return ResponseEntity.badRequest().build();
        }
        movimientoDTO.setTipoMovimiento("TRANSFERENCIA");
        return registrarMovimiento(movimientoDTO);
    }
}

