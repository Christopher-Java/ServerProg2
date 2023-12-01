package com.yrgo.domain.test;


import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/other-tiers.xml", "/datasource-test.xml"})
@Transactional
public class CustomerTest {

    @Autowired
    private CustomerManagementService customer;

    @Test
    public void creatingNewCustomer() {
        String customerId = "123";
        Customer testCust = new Customer(customerId, "Acme Ab", "acmeab@fakemail.com", "123456789", "Hello");
        customer.newCustomer(testCust);

        Customer foundCust = null;
        try {
            foundCust = customer.findCustomerById(customerId);
            assertEquals(testCust, foundCust, "The customer returned is the wrong one!");
        } catch (CustomerNotFoundException e) {
            fail("No customer was found when one should have been!");
        }
    }

    @Test
    public void deleteCustomer() {
        String customerId = "123";
        Customer testCust = new Customer(customerId, "Acme Ab", "acmeab@fakemail.com", "123456789", "Hello");
        customer.newCustomer(testCust);

        try {
            customer.deleteCustomer(testCust);
            Customer findCust = customer.findCustomerById(customerId);
            assertNull(findCust);

        } catch (CustomerNotFoundException | RecordNotFoundException e) {
            System.out.println("The customer couldnt be found");
        }

    }

}
