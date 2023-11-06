package com.yrgo.client;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementMockImpl;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleClient {
    public static void main(String[] args) throws CustomerNotFoundException {
        
        String customerID = "OB74";
        String newCustomerID = "NK90";
        
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");
        CustomerManagementService cms = container.getBean(CustomerManagementService.class);
        
        // System.out.println(cms.findCustomersByName(customerID));
        // System.out.println(cms.getFullCustomerDetail(customerID));
        cms.getAllCustomers().forEach(System.out::println);
        container.close();
    }
}
