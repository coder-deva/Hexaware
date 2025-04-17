package com.hexaware.carconnect.service;

import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.service.CustomerService;
import com.hexaware.carconnect.exception.DatabaseConnectionException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    private static CustomerService customerService;

    @BeforeAll
    public static void setup() throws DatabaseConnectionException {
        customerService = new CustomerService();
    }

    @Test
    public void testAuthenticationWithInvalidCredentials() {
        Customer customer = customerService.getCustomerByUsername("invalidUser");
        assertNull(customer, "Customer should not be found with invalid username");
    }

    @Test
    public void testUpdateCustomerInformation() {
        Customer customer = customerService.getCustomerById(1);
        if (customer != null) {
            customer.setPhoneNumber("1234567890");
            boolean updated = customerService.updateCustomer(customer);
            assertTrue(updated, "Customer should be updated successfully");
        } else {
            fail("Customer not found for update test");
        }
    }
}