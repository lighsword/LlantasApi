package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.*;
import com.proyectoMaycollins.LlantasApi.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VentaService {
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final UserService userService;

    public @NonNull Venta crearVenta(Venta venta, List<DetalleVenta> detalles) {
        log.info("Creando nueva venta para cliente ID: {}", venta.getCliente().getClienteId());
        
        // Validar cliente
        Cliente cliente = clienteRepository.findById(venta.getCliente().getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        // Validar usuario
        User usuario = userService.findByUsername(venta.getUsuario().getUsername());
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        // Establecer fecha de venta
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado(EstadoVenta.PENDIENTE);
        
        // Calcular total
        double total = detalles.stream()
                .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario())
                .sum();
        venta.setTotal(total);
        
        // Guardar venta
        Venta ventaGuardada = ventaRepository.save(venta);
        
        // Actualizar detalles con el ID de la venta
        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(ventaGuardada);
            
            // Actualizar stock del producto
            Producto producto = productoRepository.findById(detalle.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
            }
            
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
            
            detalleVentaRepository.save(detalle);
        }
        
        // Actualizar estado a completada
        ventaGuardada.setEstado(EstadoVenta.COMPLETADA);
        return ventaRepository.save(ventaGuardada);
    }

    public @NonNull Venta encontrarPorId(Long id) {
        log.info("Buscando venta con ID: {}", id);
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    public List<Venta> listarTodas() {
        log.info("Listando todas las ventas");
        return ventaRepository.findAll();
    }

    public List<DetalleVenta> listarDetallesVenta(Long ventaId) {
        log.info("Listando detalles de venta con ID: {}", ventaId);
        return detalleVentaRepository.findByVenta_VentaId(ventaId);
    }
}