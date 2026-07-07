package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.Customer;
import com.feelt.fleet.model.CustomerStatus;
import com.feelt.fleet.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> all() {
        return customerRepository.findAll();
    }

    @GetMapping("/page")
    public Page<Customer> page(@RequestParam(required = false) CustomerStatus status,
                               @RequestParam(defaultValue = "") String search,
                               Pageable pageable) {
        if (status != null) {
            return customerRepository.findByStatus(status, pageable);
        }
        if (!search.isBlank()) {
            return customerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        }
        return customerRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Customer one(@PathVariable Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody Customer customer) {
        if (customer.getStatus() == null) {
            customer.setStatus(CustomerStatus.ACTIVE);
        }
        return customerRepository.save(customer);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @Valid @RequestBody Customer updated) {
        Customer customer = one(id);
        customer.setName(updated.getName());
        customer.setEmail(updated.getEmail());
        customer.setPhone(updated.getPhone());
        customer.setCompanyName(updated.getCompanyName());
        customer.setBillingAddress(updated.getBillingAddress());
        customer.setStatus(updated.getStatus() == null ? CustomerStatus.ACTIVE : updated.getStatus());
        return customerRepository.save(customer);
    }

    @PatchMapping("/{id}/status/{status}")
    public Customer status(@PathVariable Long id, @PathVariable CustomerStatus status) {
        Customer customer = one(id);
        customer.setStatus(status);
        return customerRepository.save(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }
}
