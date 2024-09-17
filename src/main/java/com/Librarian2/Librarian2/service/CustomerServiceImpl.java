package com.Librarian2.Librarian2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Librarian2.Librarian2.dao.CustomerDao;
import com.Librarian2.Librarian2.models.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public List<Customer> getCustomer() {
		// TODO Auto-generated method stub
		return customerDao.findAll();
	}

	@Override
	public Customer addCustomer(Customer customer) {
		// TODO Auto-generated method stub
		customerDao.save(customer);
		return customer;
	}

}
