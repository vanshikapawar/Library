package com.Librarian2.Librarian2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Librarian2.Librarian2.dao.CustomerDao;
import com.Librarian2.Librarian2.models.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Override
	public List<Customer> getCustomer() {
		// TODO Auto-generated method stub
		return customerDao.findAll();
	}

	@Override
	public Customer addCustomer(Customer customer) {
		// TODO Auto-generated method stub
		customerDao.save(customer);
		sendCustomMail(customer);
		return customer;
	}
	
	private void sendCustomMail(Customer customer) {
        String email = customer.getEmail();  // Assuming there's an getEmail() method
        String subject = "Welcome to Our Library";
        String message = "Dear " + customer.getCust_name() + ",\n\n" +
                         "Thank you for registering with our library. We hope you enjoy exploring our collection of books.\n\n" +
                         "Best regards,\nLibrary Team";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

}
