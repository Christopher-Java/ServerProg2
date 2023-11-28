package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CustomerDaoJPAImpl implements CustomerDao {
    EntityManager em;

    @Override
    public void create(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        String jpaQuery = "SELECT c FROM Customer c WHERE c.customerId = :customerId";

        if (jpaQuery == null) {
            throw new RecordNotFoundException();
        }
        return em.createQuery(jpaQuery, Customer.class).setParameter("customerId", customerId).getSingleResult();
    }

    @Override
    public List<Customer> getByName(String name) {
        String jpaQuery = "SELECT c FROM Customer c WHERE c.companyName = :name ";

        return em.createQuery(jpaQuery, Customer.class).setParameter("name", name).getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        Customer updatingCustomer = em.find(Customer.class, customerToUpdate.getCustomerId());

        updatingCustomer.setCompanyName(customerToUpdate.getCompanyName());
        updatingCustomer.setEmail(customerToUpdate.getEmail());
        updatingCustomer.setCalls(customerToUpdate.getCalls());
        updatingCustomer.setNotes(customerToUpdate.getNotes());
        updatingCustomer.setTelephone(customerToUpdate.getTelephone());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        Customer deleteCustomer = em.find(Customer.class, oldCustomer.getCustomerId());
        em.remove(deleteCustomer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em.createQuery("SELECT customer FROM Customer AS customer", Customer.class).getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        return em.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.calls WHERE c.customerId = :customerId", Customer.class)
                .setParameter("customerId", customerId).getSingleResult();
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        customer.addCall(newCall);
        em.merge(customer);

    }

}
