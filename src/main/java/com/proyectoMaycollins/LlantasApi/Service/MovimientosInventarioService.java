package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.DTO.MovimientosInventarioDTO;
import com.proyectoMaycollins.LlantasApi.DTO.RegistrarMovimientoDTO;
import com.proyectoMaycollins.LlantasApi.DTO.ResumenAlmacenDTO;
import com.proyectoMaycollins.LlantasApi.Model.Inventario;
import com.proyectoMaycollins.LlantasApi.Model.MovimientosInventario;
import com.proyectoMaycollins.LlantasApi.Repository.InventarioRepository;
import com.proyectoMaycollins.LlantasApi.Repository.MovimientosInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientosInventarioService {

    private final MovimientosInventarioRepository movimientosRepository;
    private final InventarioRepository inventarioRepository;
    private final ProductoVisibilidadService visibilidadService; // ✨ NUEVO

    public MovimientosInventarioService(
            MovimientosInventarioRepository movimientosRepository,
            InventarioRepository inventarioRepository,
            ProductoVisibilidadService visibilidadService) { // ✨ NUEVO
        this.movimientosRepository = movimientosRepository;
        this.inventarioRepository = inventarioRepository;
        this.visibilidadService = visibilidadService; // ✨ NUEVO
    }

    /**
     * Registrar un nuevo movimiento de inventario
     * Actualiza automáticamente el stock en la tabla inventario
     * ✨ NUEVO: Notifica si el producto se agota
     */
    @Transactional
    public MovimientosInventarioDTO registrarMovimiento(RegistrarMovimientoDTO dto) {
        // Validar tipo de movimiento
        if (!List.of("ENTRADA", "SALIDA", "AJUSTE", "TRANSFERENCIA").contains(dto.getTipoMovimiento())) {
            throw new IllegalArgumentException("Tipo de movimiento inválido");
        }

        // Obtener stock actual
        Inventario.InventarioId inventarioId = new Inventario.InventarioId();
        inventarioId.setProductoId(dto.getProductoId().intValue());
        inventarioId.setAlmacenId(dto.getAlmacenId());

        Inventario inventario = inventarioRepository.findById(inventarioId)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setProductoId(dto.getProductoId().intValue());
                    nuevo.setAlmacenId(dto.getAlmacenId());
                    nuevo.setCantidad(0);
                    nuevo.setFechaActualizacion(OffsetDateTime.now());
                    return nuevo;
                });

        int cantidadAnterior = inventario.getCantidad() != null ? inventario.getCantidad() : 0;
        int cantidadNueva;

        // Calcular nueva cantidad según tipo de movimiento
        switch (dto.getTipoMovimiento()) {
            case "ENTRADA":
                cantidadNueva = cantidadAnterior + dto.getCantidad();
                break;
            case "SALIDA":
                cantidadNueva = cantidadAnterior - dto.getCantidad();
                if (cantidadNueva < 0) {
                    throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + cantidadAnterior);
                }
                break;
            case "AJUSTE":
                cantidadNueva = dto.getCantidad();
                break;
            case "TRANSFERENCIA":
                if (dto.getAlmacenDestinoId() == null) {
                    throw new IllegalArgumentException("Almacén destino requerido para transferencias");
                }
                cantidadNueva = cantidadAnterior - dto.getCantidad();
                if (cantidadNueva < 0) {
                    throw new IllegalArgumentException("Stock insuficiente para transferencia");
                }
                // Registrar entrada en almacén destino
                registrarEntradaTransferencia(dto);
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no soportado");
        }

        // Crear movimiento
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setAlmacenId(dto.getAlmacenId());
        movimiento.setProductoId(dto.getProductoId());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setCantidadAnterior(cantidadAnterior);
        movimiento.setCantidadNueva(cantidadNueva);
        movimiento.setFechaMovimiento(OffsetDateTime.now());
        movimiento.setUsuarioId(dto.getUsuarioId());
        movimiento.setReferenciaTipo(dto.getReferenciaTipo());
        movimiento.setReferenciaId(dto.getReferenciaId());
        movimiento.setAlmacenDestinoId(dto.getAlmacenDestinoId());
        movimiento.setObservaciones(dto.getObservaciones());

        movimiento = movimientosRepository.save(movimiento);

        // Actualizar inventario
        inventario.setCantidad(cantidadNueva);
        inventario.setFechaActualizacion(OffsetDateTime.now());
        inventarioRepository.save(inventario);

        // ✨ NUEVO: Verificar stock total y notificar si está agotado
        visibilidadService.verificarYNotificarStock(dto.getProductoId());

        return convertToDTO(movimiento);
    }

    /**
     * Registrar entrada en almacén destino para transferencias
     */
    private void registrarEntradaTransferencia(RegistrarMovimientoDTO dtoOrigen) {
        RegistrarMovimientoDTO dtoDestino = new RegistrarMovimientoDTO();
        dtoDestino.setAlmacenId(dtoOrigen.getAlmacenDestinoId());
        dtoDestino.setProductoId(dtoOrigen.getProductoId());
        dtoDestino.setTipoMovimiento("ENTRADA");
        dtoDestino.setCantidad(dtoOrigen.getCantidad());
        dtoDestino.setUsuarioId(dtoOrigen.getUsuarioId());
        dtoDestino.setReferenciaTipo("TRANSFERENCIA");
        dtoDestino.setReferenciaId(dtoOrigen.getReferenciaId());
        dtoDestino.setObservaciones("Transferencia desde almacén " + dtoOrigen.getAlmacenId());

        // Recursión controlada (sin almacenDestinoId para evitar loop)
        Inventario.InventarioId inventarioId = new Inventario.InventarioId();
        inventarioId.setProductoId(dtoDestino.getProductoId().intValue());
        inventarioId.setAlmacenId(dtoDestino.getAlmacenId());

        Inventario inventario = inventarioRepository.findById(inventarioId)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setProductoId(dtoDestino.getProductoId().intValue());
                    nuevo.setAlmacenId(dtoDestino.getAlmacenId());
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        int cantidadAnterior = inventario.getCantidad() != null ? inventario.getCantidad() : 0;
        int cantidadNueva = cantidadAnterior + dtoDestino.getCantidad();

        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setAlmacenId(dtoDestino.getAlmacenId());
        movimiento.setProductoId(dtoDestino.getProductoId());
        movimiento.setTipoMovimiento(dtoDestino.getTipoMovimiento());
        movimiento.setCantidad(dtoDestino.getCantidad());
        movimiento.setCantidadAnterior(cantidadAnterior);
        movimiento.setCantidadNueva(cantidadNueva);
        movimiento.setUsuarioId(dtoDestino.getUsuarioId());
        movimiento.setReferenciaTipo(dtoDestino.getReferenciaTipo());
        movimiento.setReferenciaId(dtoDestino.getReferenciaId());
        movimiento.setObservaciones(dtoDestino.getObservaciones());

        movimientosRepository.save(movimiento);

        inventario.setCantidad(cantidadNueva);
        inventario.setFechaActualizacion(OffsetDateTime.now());
        inventarioRepository.save(inventario);
    }

    /**
     * Obtener todos los movimientos de un almacén
     */
    public List<MovimientosInventarioDTO> getMovimientosByAlmacen(Integer almacenId) {
        return movimientosRepository.findByAlmacenIdOrderByFechaMovimientoDesc(almacenId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener movimientos de un producto
     */
    public List<MovimientosInventarioDTO> getMovimientosByProducto(Long productoId) {
        return movimientosRepository.findByProductoIdOrderByFechaMovimientoDesc(productoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener movimientos por rango de fechas
     */
    public List<MovimientosInventarioDTO> getMovimientosByFechas(
            OffsetDateTime fechaInicio, OffsetDateTime fechaFin) {
        return movimientosRepository.findByFechaMovimientoBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener movimiento por ID
     */
    public MovimientosInventarioDTO getMovimientoById(Long id) {
        return movimientosRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Convertir entidad a DTO
     */
    private MovimientosInventarioDTO convertToDTO(MovimientosInventario movimiento) {
        MovimientosInventarioDTO dto = new MovimientosInventarioDTO();
        dto.setMovimientoId(movimiento.getMovimientoId());
        dto.setAlmacenId(movimiento.getAlmacenId());
        dto.setProductoId(movimiento.getProductoId());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setCantidad(movimiento.getCantidad());
        dto.setCantidadAnterior(movimiento.getCantidadAnterior());
        dto.setCantidadNueva(movimiento.getCantidadNueva());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        dto.setUsuarioId(movimiento.getUsuarioId());
        dto.setReferenciaTipo(movimiento.getReferenciaTipo());
        dto.setReferenciaId(movimiento.getReferenciaId());
        dto.setAlmacenDestinoId(movimiento.getAlmacenDestinoId());
        dto.setObservaciones(movimiento.getObservaciones());

        // Cargar nombres si las relaciones están disponibles
        if (movimiento.getAlmacen() != null) {
            dto.setAlmacenNombre(movimiento.getAlmacen().getNombre());
        }
        if (movimiento.getProducto() != null) {
            dto.setProductoDescripcion(movimiento.getProducto().getDescripcion());
        }
        if (movimiento.getUsuario() != null) {
            dto.setUsuarioNombre(movimiento.getUsuario().getNombre());
        }
        if (movimiento.getAlmacenDestino() != null) {
            dto.setAlmacenDestinoNombre(movimiento.getAlmacenDestino().getNombre());
        }

        return dto;
    }
}

