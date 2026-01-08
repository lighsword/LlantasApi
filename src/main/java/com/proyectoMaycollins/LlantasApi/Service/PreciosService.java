package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.DTO.ActualizarPreciosProductoDTO;
import com.proyectoMaycollins.LlantasApi.DTO.PrecioDTO;
import com.proyectoMaycollins.LlantasApi.DTO.PreciosProductoDTO;
import com.proyectoMaycollins.LlantasApi.Model.Precios;
import com.proyectoMaycollins.LlantasApi.Model.Productos;
import com.proyectoMaycollins.LlantasApi.Model.enums.TipoPrecio;
import com.proyectoMaycollins.LlantasApi.Repository.PreciosRepository;
import com.proyectoMaycollins.LlantasApi.Repository.ProductosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreciosService {

    private final PreciosRepository preciosRepository;
    private final ProductosRepository productosRepository;

    /**
     * Crear o actualizar un precio individual
     */
    @Transactional
    public PrecioDTO guardarPrecio(PrecioDTO precioDTO) {
        // Validar que el producto existe
        Productos producto = productosRepository.findById(precioDTO.getProductosId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + precioDTO.getProductosId()));

        // Desactivar precios anteriores del mismo tipo
        preciosRepository.desactivarPreciosAnteriores(precioDTO.getProductosId(), precioDTO.getTipo());

        // Crear nuevo precio
        Precios precio = Precios.builder()
                .productoId(precioDTO.getProductosId())
                .tipo(precioDTO.getTipo())
                .precio(precioDTO.getPrecio())
                .fechaInicio(precioDTO.getFechaInicio() != null ? precioDTO.getFechaInicio() : OffsetDateTime.now())
                .activo(true)
                .build();

        precio = preciosRepository.save(precio);

        // También actualizar la tabla productos para mantener sincronización
        actualizarPreciosEnProducto(producto, precioDTO.getTipo(), precioDTO.getPrecio());

        return convertirADTO(precio, producto);
    }

    /**
     * Actualizar múltiples precios de un producto a la vez
     */
    @Transactional
    public PreciosProductoDTO actualizarTodosLosPrecios(ActualizarPreciosProductoDTO dto) {
        Productos producto = productosRepository.findById(dto.getProductosId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + dto.getProductosId()));

        // Actualizar precio de compra
        if (dto.getPrecioCompra() != null) {
            guardarPrecioInterno(dto.getProductosId(), TipoPrecio.COMPRA, dto.getPrecioCompra());
            producto.setPrecioCompra(dto.getPrecioCompra());
        }

        // Actualizar precio de venta
        if (dto.getPrecioVenta() != null) {
            guardarPrecioInterno(dto.getProductosId(), TipoPrecio.VENTA, dto.getPrecioVenta());
            producto.setPrecioVenta(dto.getPrecioVenta());
        }

        // Actualizar precio mayorista
        if (dto.getPrecioMayorista() != null) {
            guardarPrecioInterno(dto.getProductosId(), TipoPrecio.MAYORISTA, dto.getPrecioMayorista());
            producto.setPrecioMayorista(dto.getPrecioMayorista());
        }

        productosRepository.save(producto);

        return obtenerPreciosProducto(dto.getProductosId());
    }

    /**
     * Obtener todos los precios activos de un producto
     */
    public PreciosProductoDTO obtenerPreciosProducto(Long productosId) {
        Productos producto = productosRepository.findById(productosId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productosId));

        List<Precios> preciosActivos = preciosRepository.findByProductoIdAndActivoTrue(productosId);

        Map<TipoPrecio, BigDecimal> preciosMap = preciosActivos.stream()
                .collect(Collectors.toMap(Precios::getTipo, Precios::getPrecio));

        BigDecimal precioCompra = preciosMap.getOrDefault(TipoPrecio.COMPRA, producto.getPrecioCompra());
        BigDecimal precioVenta = preciosMap.getOrDefault(TipoPrecio.VENTA, producto.getPrecioVenta());
        BigDecimal precioMayorista = preciosMap.getOrDefault(TipoPrecio.MAYORISTA, producto.getPrecioMayorista());

        // Calcular márgenes de ganancia
        BigDecimal margenVenta = calcularMargen(precioVenta, precioCompra);
        BigDecimal margenMayorista = calcularMargen(precioMayorista, precioCompra);
        Double porcentajeVenta = calcularPorcentajeGanancia(precioVenta, precioCompra);
        Double porcentajeMayorista = calcularPorcentajeGanancia(precioMayorista, precioCompra);

        return PreciosProductoDTO.builder()
                .productosId(productosId)
                .codigoProducto(producto.getCodigoProducto())
                .descripcion(producto.getDescripcion())
                .precioCompra(precioCompra)
                .precioVenta(precioVenta)
                .precioMayorista(precioMayorista)
                .margenVenta(margenVenta)
                .margenMayorista(margenMayorista)
                .porcentajeGananciaVenta(porcentajeVenta)
                .porcentajeGananciaMayorista(porcentajeMayorista)
                .marca(producto.getMarca())
                .modelo(producto.getModelo())
                .activo(producto.getActivo())
                .build();
    }

    /**
     * Obtener precio activo por tipo
     */
    public PrecioDTO obtenerPrecioPorTipo(Long productosId, TipoPrecio tipo) {
        Productos producto = productosRepository.findById(productosId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productosId));

        Precios precio = preciosRepository.findByProductoIdAndTipoAndActivoTrue(productosId, tipo)
                .orElseThrow(() -> new RuntimeException("No se encontró precio activo de tipo " + tipo + " para el producto"));

        return convertirADTO(precio, producto);
    }

    /**
     * Obtener historial de precios de un producto
     */
    public List<PrecioDTO> obtenerHistorialPrecios(Long productosId, TipoPrecio tipo) {
        Productos producto = productosRepository.findById(productosId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productosId));

        List<Precios> historial = preciosRepository.obtenerHistorialPrecios(productosId, tipo);

        return historial.stream()
                .map(precio -> convertirADTO(precio, producto))
                .collect(Collectors.toList());
    }

    /**
     * Listar todos los precios activos
     */
    public List<PrecioDTO> listarPreciosActivos() {
        List<Precios> precios = preciosRepository.findAll().stream()
                .filter(Precios::getActivo)
                .collect(Collectors.toList());

        return precios.stream()
                .map(precio -> {
                    Productos producto = productosRepository.findById(precio.getProductoId()).orElse(null);
                    return convertirADTO(precio, producto);
                })
                .collect(Collectors.toList());
    }

    /**
     * Listar todos los precios de productos con cálculo de márgenes
     */
    public List<PreciosProductoDTO> listarTodosPreciosProductos() {
        List<Productos> productos = productosRepository.findAll();

        return productos.stream()
                .map(producto -> obtenerPreciosProducto(producto.getProductoId()))
                .collect(Collectors.toList());
    }

    /**
     * Desactivar un precio específico
     */
    @Transactional
    public void desactivarPrecio(Long precioId) {
        Precios precio = preciosRepository.findById(precioId)
                .orElseThrow(() -> new RuntimeException("Precio no encontrado con ID: " + precioId));

        precio.setActivo(false);
        preciosRepository.save(precio);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void guardarPrecioInterno(Long productosId, TipoPrecio tipo, BigDecimal precio) {
        preciosRepository.desactivarPreciosAnteriores(productosId, tipo);

        Precios nuevoPrecio = Precios.builder()
                .productoId(productosId)
                .tipo(tipo)
                .precio(precio)
                .fechaInicio(OffsetDateTime.now())
                .activo(true)
                .build();

        preciosRepository.save(nuevoPrecio);
    }

    private void actualizarPreciosEnProducto(Productos producto, TipoPrecio tipo, BigDecimal precio) {
        switch (tipo) {
            case COMPRA:
                producto.setPrecioCompra(precio);
                break;
            case VENTA:
                producto.setPrecioVenta(precio);
                break;
            case MAYORISTA:
                producto.setPrecioMayorista(precio);
                break;
        }
        productosRepository.save(producto);
    }

    private BigDecimal calcularMargen(BigDecimal precioVenta, BigDecimal precioCompra) {
        if (precioVenta == null || precioCompra == null) {
            return BigDecimal.ZERO;
        }
        return precioVenta.subtract(precioCompra);
    }

    private Double calcularPorcentajeGanancia(BigDecimal precioVenta, BigDecimal precioCompra) {
        if (precioVenta == null || precioCompra == null || precioCompra.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        BigDecimal ganancia = precioVenta.subtract(precioCompra);
        BigDecimal porcentaje = ganancia.divide(precioCompra, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        return porcentaje.doubleValue();
    }

    private PrecioDTO convertirADTO(Precios precio, Productos producto) {
        return PrecioDTO.builder()
                .preciosId(precio.getPrecioId())
                .productosId(precio.getProductoId())
                .tipo(precio.getTipo())
                .precio(precio.getPrecio())
                .fechaInicio(precio.getFechaInicio())
                .activo(precio.getActivo())
                .codigoProducto(producto != null ? producto.getCodigoProducto() : null)
                .descripcionProducto(producto != null ? producto.getDescripcion() : null)
                .build();
    }
}

