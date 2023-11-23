package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class CustomerDaoJdbcTemplateImpl implements CustomerDao {

    private static final String CUSTOMER_ID_SQL = "SELECT CUSTOMER_ID FROM CUSTOMER WHERE CUSTOMER_ID = ?";
    private static final String COMPANY_NAME_SQL = "SELECT COMPANY_NAME FROM CUSTOMER WHERE COMPANY_NAME = ? ";
    private static final String UPDATE_SQL = "UPDATE CUSTOMER SET CUSTOMER_ID=?, COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMER_ID=?";
    private static final String INSERT_SQL = "INSERT INTO CUSTOMER (COMPANY_NAME, EMAIL, TELEPHONE,NOTES) VALUES (?,?,?,?)";
    private static final String DELETE_SQL = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";
    private static final String SELECT_ALL = "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER";
    private static final String SELECT_ALL_CALLS = "SELECT CALL_ID, NOTES, TIME_DATE FROM TBL_CALL";
    private static final String INSERT_SQL_CALL = "INSERT INTO TBL_CALL ( TIME_DATE, NOTES, CUSTOMER_ID) VALUES (?,?,?)";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void createTables() {
        try {
            this.jdbcTemplate.update("CREATE TABLE IF NOT EXISTS CUSTOMER (CUSTOMER_ID VARCHAR(255), COMPANY_NAME VARCHAR(255), EMAIL VARCHAR(50), TELEPHONE VARCHAR(15), NOTES VARCHAR(255))");
        } catch (org.springframework.jdbc.BadSqlGrammarException e) {
            System.out.println("Something went wrong with creating table CUSTOMER");
        }

        try {
            this.jdbcTemplate.update("CREATE TABLE IF NOT EXISTS TBL_CALL (NOTES VARCHAR(255), TIME_DATE DATE, CUSTOMER_ID VARCHAR(255))");
        } catch (org.springframework.jdbc.BadSqlGrammarException e) {
            System.out.println("Something went wrong with creating table TBL_CALL");
        }
    }

    @Override
    public void create(Customer customer) {
        jdbcTemplate.update(INSERT_SQL, customer.getCompanyName(), customer.getEmail(), customer.getTelephone(), customer.getNotes());


    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {

        return jdbcTemplate.queryForObject(CUSTOMER_ID_SQL, new CustomerRowMapper(), customerId);
    }

    @Override
    public List<Customer> getByName(String name) {
        return jdbcTemplate.query(COMPANY_NAME_SQL, new CustomerRowMapper(), name);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        this.jdbcTemplate.update(UPDATE_SQL, customerToUpdate.getEmail(), customerToUpdate.getCalls(), customerToUpdate.getTelephone(), customerToUpdate.getNotes(), customerToUpdate.getCustomerId(), customerToUpdate.getCompanyName());

    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        this.jdbcTemplate.update(DELETE_SQL, oldCustomer.getCustomerId());

    }

    @Override
    public List<Customer> getAllCustomers() {
        return this.jdbcTemplate.query(SELECT_ALL, new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Customer customer = jdbcTemplate.queryForObject(SELECT_ALL, new CustomerRowMapper(), customerId);
        List<Call> calls = jdbcTemplate.query(SELECT_ALL_CALLS, new CallRowMapper());
        assert customer != null;
        customer.setCalls(calls);

        return customer;
    }


    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        this.jdbcTemplate.update(INSERT_SQL_CALL, newCall.getTimeAndDate(), newCall.getNotes(), customerId);
    }
}

class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int i) throws SQLException {
        String customerId = rs.getString(1);
        String companyName = rs.getString(2);
        String email = rs.getString(3);
        String telephone = rs.getString(4);
        String notes = rs.getString(5);

        return new Customer("" + customerId, companyName, email, telephone, notes);
    }

}

class CallRowMapper implements RowMapper<Call> {

    @Override
    public Call mapRow(ResultSet rs, int i) throws SQLException {
        int callId = rs.getInt(1);
        Date date = rs.getDate(2);
        String notes = rs.getString(3);

        return new Call("" + callId + notes, date);
    }
}

