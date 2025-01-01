package com.maycollins.LlantasApi.controller;

import com.maycollins.LlantasApi.DTO.CustomerDTO;
import com.maycollins.LlantasApi.DTO.CustomerResponseDTO;
import com.maycollins.LlantasApi.DTO.SaleHistoryDTO;
import com.maycollins.LlantasApi.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Integer id,
            @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CustomerResponseDTO>> getCustomersByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(customerService.getCustomersByCategory(category));
    }

    @GetMapping("/{id}/sales-history")
    public ResponseEntity<List<SaleHistoryDTO>> getCustomerSaleHistory(
            @PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerSaleHistory(id));
    }

    @PutMapping("/{id}/update-category")
    public ResponseEntity<Void> updateCustomerCategory(@PathVariable Integer id) {
        customerService.updateCustomerCategory(id);
        return ResponseEntity.ok().build();
    }
}