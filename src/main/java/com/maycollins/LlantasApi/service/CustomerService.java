package com.maycollins.LlantasApi.service;

import com.maycollins.LlantasApi.DTO.CustomerDTO;
import com.maycollins.LlantasApi.DTO.CustomerResponseDTO;
import com.maycollins.LlantasApi.DTO.SaleHistoryDTO;
import com.maycollins.LlantasApi.model.Customer;
import com.maycollins.LlantasApi.model.Sale;
import com.maycollins.LlantasApi.repository.CustomerRepository;
import com.maycollins.LlantasApi.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;

    public CustomerService(CustomerRepository customerRepository, SaleRepository saleRepository) {
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
    }

    public CustomerResponseDTO createCustomer(CustomerDTO customerDTO) {
        validateCustomerEmail(customerDTO.getEmail());
        Customer customer = convertToEntity(customerDTO);
        customer.setCustomerStatus("Activo");
        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponseDTO(savedCustomer);
    }

    public CustomerResponseDTO getCustomerById(Integer id) {
        Customer customer = findCustomerById(id);
        return convertToResponseDTO(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO updateCustomer(Integer id, CustomerDTO customerDTO) {
        Customer customer = findCustomerById(id);
        validateEmailForUpdate(customer, customerDTO.getEmail());
        updateCustomerFields(customer, customerDTO);
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponseDTO(updatedCustomer);
    }

    public void deleteCustomer(Integer id) {
        Customer customer = findCustomerById(id);
        customer.setCustomerStatus("Inactivo");
        customerRepository.save(customer);
    }

    public List<CustomerResponseDTO> getCustomersByCategory(String category) {
        return customerRepository.findByCustomerCategory(category)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SaleHistoryDTO> getCustomerSaleHistory(Integer customerId) {
        return saleRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(this::convertToSaleHistoryDTO)
                .collect(Collectors.toList());
    }

    public void updateCustomerCategory(Integer customerId) {
        Customer customer = findCustomerById(customerId);
        BigDecimal totalPurchases = customerRepository.calculateTotalPurchases(customerId);
        Integer totalTransactions = customerRepository.countTransactions(customerId);

        // Lógica para actualizar la categoría basada en el historial de compras
        if (totalPurchases.compareTo(new BigDecimal("10000")) > 0 && totalTransactions > 20) {
            customer.setCustomerCategory("Premium");
        } else if (totalPurchases.compareTo(new BigDecimal("5000")) > 0 && totalTransactions > 10) {
            customer.setCustomerCategory("Mayorista");
        } else {
            customer.setCustomerCategory("Regular");
        }

        customerRepository.save(customer);
    }

    private Customer findCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private void validateCustomerEmail(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
    }

    private void validateEmailForUpdate(Customer customer, String newEmail) {
        if (!customer.getEmail().equals(newEmail) &&
                customerRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("Email already exists");
        }
    }

    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setCustomerName(dto.getCustomerName());
        customer.setCustomerLastname(dto.getCustomerLastname());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setSaleHistory(dto.getSaleHistory());
        customer.setCustomerCategory(dto.getCustomerCategory());
        customer.setCustomerStatus(dto.getCustomerStatus());
        customer.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        return customer;
    }

    private void updateCustomerFields(Customer customer, CustomerDTO dto) {
        customer.setCustomerName(dto.getCustomerName());
        customer.setCustomerLastname(dto.getCustomerLastname());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
    }

    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        List<SaleHistoryDTO> salesHistory = getCustomerSaleHistory(customer.getCustomerId());
        BigDecimal totalPurchases = customerRepository.calculateTotalPurchases(customer.getCustomerId());
        Integer totalTransactions = customerRepository.countTransactions(customer.getCustomerId());

        return new CustomerResponseDTO(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getCustomerLastname(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getSaleHistory(),
                customer.getCustomerCategory(),
                customer.getCustomerStatus(),
                customer.getPreferredPaymentMethod(),
                salesHistory,
                totalPurchases,
                totalTransactions
        );
    }

    private SaleHistoryDTO convertToSaleHistoryDTO(Sale sale) {
        return new SaleHistoryDTO(
                sale.getSaleId(),
                sale.getSaleDate(),
                sale.getProduct().getProductSerial(),
                sale.getSoldQuantity(),
                sale.getTotal(),
                sale.getPaymentMethod(),
                "Completada"
        );
    }
}