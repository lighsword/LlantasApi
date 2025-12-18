package com.proyectoMaycollins.LlantasApi.Config;

import com.proyectoMaycollins.LlantasApi.Model.*;
import com.proyectoMaycollins.LlantasApi.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StartupMessage implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaProductoRepository categoriaProductoRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        printAllEndpoints(event);
    }

    private void printAllEndpoints(ContextRefreshedEvent event) {
        try {
            RequestMappingHandlerMapping requestMappingHandlerMapping = 
                event.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
            
            Map<RequestMappingInfo, org.springframework.web.method.HandlerMethod> map = 
                requestMappingHandlerMapping.getHandlerMethods();
                
            map.forEach((requestMappingInfo, handlerMethod) -> {
                String patterns = "N/A";
                // Verificar si getPatternsCondition() devuelve null antes de llamar a getPatterns()
                if (requestMappingInfo.getPatternsCondition() != null) {
                    patterns = String.join(", ", requestMappingInfo.getPatternsCondition().getPatterns());
                }
                
                System.out.println("Endpoint: " + patterns + " -> " + handlerMethod.getMethod().getName());
            });
        } catch (Exception e) {
            System.out.println("Could not print endpoints: " + e.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inicializando datos de ejemplo...");
        
        // Crear categorías de productos si no existen
        if (categoriaProductoRepository.count() == 0) {
            List<CategoriaProducto> categorias = Arrays.asList(
                CategoriaProducto.builder().nombre("Neumáticos Automotrices").descripcion("Llantas para automóviles").activo(true).build(),
                CategoriaProducto.builder().nombre("Neumáticos Camiones").descripcion("Llantas para camiones pesados").activo(true).build(),
                CategoriaProducto.builder().nombre("Neumáticos Motos").descripcion("Llantas para motocicletas").activo(true).build(),
                CategoriaProducto.builder().nombre("Neumáticos Agrícolas").descripcion("Llantas para maquinaria agrícola").activo(true).build(),
                CategoriaProducto.builder().nombre("Neumáticos Industriales").descripcion("Llantas para equipos industriales").activo(true).build()
            );
            categoriaProductoRepository.saveAll(categorias);
            System.out.println("Categorías creadas: " + categorias.size());
        }

        // Crear usuarios si no existen
        if (userRepository.count() == 0) {
            List<User> users = Arrays.asList(
                User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build(),
                User.builder()
                    .username("yordan")
                    .password(passwordEncoder.encode("yordan123"))
                    .role(Role.ADMIN)  // Usuario yordan con rol de admin como solicitaste
                    .enabled(true)
                    .build(),
                User.builder()
                    .username("cliente1")
                    .password(passwordEncoder.encode("cliente123"))
                    .role(Role.USER)
                    .enabled(true)
                    .build(),
                User.builder()
                    .username("cliente2")
                    .password(passwordEncoder.encode("cliente123"))
                    .role(Role.USER)
                    .enabled(true)
                    .build(),
                User.builder()
                    .username("cliente3")
                    .password(passwordEncoder.encode("cliente123"))
                    .role(Role.USER)
                    .enabled(true)
                    .build()
            );
            userRepository.saveAll(users);
            System.out.println("Usuarios creados: " + users.size());
        }

        // Crear clientes si no existen
        if (clienteRepository.count() == 0) {
            List<Cliente> clientes = Arrays.asList(
                Cliente.builder()
                    .nombre("Carlos Rodríguez")
                    .documentoIdentidad("12345678")
                    .telefono("987654321")
                    .email("carlos@email.com")
                    .direccion("Av. Principal 123")
                    .activo(true)
                    .build(),
                Cliente.builder()
                    .nombre("María González")
                    .documentoIdentidad("87654321")
                    .telefono("912345678")
                    .email("maria@email.com")
                    .direccion("Calle Secundaria 456")
                    .activo(true)
                    .build(),
                Cliente.builder()
                    .nombre("José Martínez")
                    .documentoIdentidad("11223344")
                    .telefono("956789123")
                    .email("jose@email.com")
                    .direccion("Jr. Comercial 789")
                    .activo(true)
                    .build(),
                Cliente.builder()
                    .nombre("Ana López")
                    .documentoIdentidad("44332211")
                    .telefono("987123456")
                    .email("ana@email.com")
                    .direccion("Psj. Residencial 321")
                    .activo(true)
                    .build(),
                Cliente.builder()
                    .nombre("Pedro Sánchez")
                    .documentoIdentidad("55667788")
                    .telefono("923456789")
                    .email("pedro@email.com")
                    .direccion("Av. Industrial 654")
                    .activo(true)
                    .build()
            );
            clienteRepository.saveAll(clientes);
            System.out.println("Clientes creados: " + clientes.size());
        }

        // Crear productos si no existen
        if (productoRepository.count() == 0) {
            List<CategoriaProducto> categorias = categoriaProductoRepository.findAll();
            
            if (!categorias.isEmpty()) {
                List<Producto> productos = Arrays.asList(
                    Producto.builder()
                        .nombreProducto("Llanta Radial 195/65R15")
                        .descripcion("Llanta radial para automóvil, tamaño 195/65R15, ideal para uso urbano y carretera.")
                        .categoriaProducto(categorias.get(0))
                        .precio(120.0)
                        .stock(50)
                        .marca("Michelin")
                        .modelo("Energy XM2")
                        .imagenUrl("https://example.com/images/llanta1.jpg")
                        .activo(true)
                        .build(),
                    Producto.builder()
                        .nombreProducto("Llanta 225/60R17")
                        .descripcion("Llanta de alto rendimiento para vehículos deportivos, tamaño 225/60R17.")
                        .categoriaProducto(categorias.get(0))
                        .precio(180.0)
                        .stock(30)
                        .marca("Continental")
                        .modelo("PremiumContact 6")
                        .imagenUrl("https://example.com/images/llanta2.jpg")
                        .activo(true)
                        .build(),
                    Producto.builder()
                        .nombreProducto("Llanta para Camión 295/75R22.5")
                        .descripcion("Llanta radial para camiones pesados, resistente y duradera.")
                        .categoriaProducto(categorias.get(1))
                        .precio(450.0)
                        .stock(20)
                        .marca("Goodyear")
                        .modelo("G277")
                        .imagenUrl("https://example.com/images/llanta3.jpg")
                        .activo(true)
                        .build(),
                    Producto.builder()
                        .nombreProducto("Llanta Moto 110/70R17")
                        .descripcion("Llanta para motocicleta deportiva, tamaño 110/70R17.")
                        .categoriaProducto(categorias.get(2))
                        .precio(80.0)
                        .stock(40)
                        .marca("Pirelli")
                        .modelo("Diablo Rosso III")
                        .imagenUrl("https://example.com/images/llanta4.jpg")
                        .activo(true)
                        .build(),
                    Producto.builder()
                        .nombreProducto("Llanta Agrícola 420/85R34")
                        .descripcion("Llanta para tractor agrícola, diseño especial para campo.")
                        .categoriaProducto(categorias.get(3))
                        .precio(600.0)
                        .stock(10)
                        .marca("Firestone")
                        .modelo("Agriboss R1/W")
                        .imagenUrl("https://example.com/images/llanta5.jpg")
                        .activo(true)
                        .build()
                );
                productoRepository.saveAll(productos);
                System.out.println("Productos creados: " + productos.size());
            } else {
                System.out.println("No se crearon productos porque no hay categorías disponibles.");
            }
        }

        System.out.println("Datos de ejemplo inicializados correctamente.");
        System.out.println("API Llantas iniciada correctamente!");
        System.out.println("Usuario administrador creado:");
        System.out.println("- Username: admin, Password: admin123");
        System.out.println("- Username: yordan, Password: yordan123 (con rol ADMIN)");
    }
}