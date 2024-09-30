package com.Librarian2.Librarian2.service;

import java.util.List;

import com.Librarian2.Librarian2.models.Customer;

public interface CustomerService {
	public List<Customer> getCustomer();
	Customer addCustomer(Customer customer);
	Customer findByEmail(String email);
	List<String> getEmailSuggestions(String query);
}
