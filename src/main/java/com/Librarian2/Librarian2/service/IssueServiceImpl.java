package com.Librarian2.Librarian2.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Librarian2.Librarian2.dao.BookDao;
import com.Librarian2.Librarian2.dao.CustomerDao;
import com.Librarian2.Librarian2.dao.IssueDao;
import com.Librarian2.Librarian2.models.Books;
import com.Librarian2.Librarian2.models.Customer;
import com.Librarian2.Librarian2.models.IssueBook;

@Service
public class IssueServiceImpl implements IssueService{
	
	@Autowired
	private IssueDao issueDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private BookDao bookDao;
	
	@Override
	public List<IssueBook> getIssuedBook() {
		// TODO Auto-generated method stub
		return issueDao.findAll();
	}

	@Override
	public IssueBook addIssueBook(IssueBook issue) {
		// TODO Auto-generated method stub
		issueDao.save(issue);
		return issue;
	}

	
	@Override
	public boolean issuedBook(String book_name, String cust_name, String email) {
		Optional<Books> optionalBook = bookDao.findByName(book_name);
		Optional<Customer> optionalCustomer = customerDao.findByNameAndEmail(cust_name, email);
        
        if (optionalBook.isPresent() && optionalCustomer.isPresent()) {
            Books book = optionalBook.get();
            
            if (book.getAvailable_copies() > 0) {
                book.setAvailable_copies(book.getAvailable_copies()-1);
                bookDao.save(book);
                
                Customer customer = optionalCustomer.get();
                IssueBook issue = new IssueBook();
                issue.setBook(book);
                issue.setCustomer(customer);
                issue.setIssue_date(Date.valueOf(LocalDate.now()));
                issue.setIdeal_return_date(Date.valueOf(LocalDate.now().plusDays(7)));
                issue.setAmt_to_be_paid(0);
                issueDao.save(issue);
                return true; 
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

	
	/*@Override
	public boolean returnBook(String book_name, String cust_name, String email) {
	    Optional<Books> optionalBook = bookDao.findByName(book_name);
	    Optional<Customer> optionalCustomer = customerDao.findByNameAndEmail(cust_name, email);

	    if (optionalBook.isPresent() && optionalCustomer.isPresent()) {
	        List<IssueBook> issued = issueDao.findAll(); 
	        Books book = optionalBook.get();
	        Customer cust = optionalCustomer.get();
	       
	        for (IssueBook issu : issued) {
	            if (issu.getBook().getBook_id() == book.getBook_id() && issu.getCustomer().getCust_id() == cust.getCust_id()) {
	                
	            	if (issu.getActual_return_date() == null) {
	            		issu.setActual_return_date(Date.valueOf(LocalDate.now()));
		                issueDao.save(issu);
		                
		                book.setAvailable_copies(book.getAvailable_copies() + 1);
		                bookDao.save(book);
		              
		                return true; 
	            	}
	            }
	        }

	        return false;
	    } else {
	        return false;
	    }
	}*/
	@Override
	public Map<String, Object> returnBook(String book_name, String cust_name, String email) {
	    Map<String, Object> response = new HashMap<>();
	    Optional<Books> optionalBook = bookDao.findByName(book_name);
	    Optional<Customer> optionalCustomer = customerDao.findByNameAndEmail(cust_name, email);

	    if (optionalBook.isPresent() && optionalCustomer.isPresent()) {
	        List<IssueBook> issued = issueDao.findAll(); 
	        Books book = optionalBook.get();
	        Customer cust = optionalCustomer.get();
	        boolean bookReturned = false;

	        for (IssueBook issu : issued) {
	            if (issu.getBook().getBook_id() == book.getBook_id() && issu.getCustomer().getCust_id() == cust.getCust_id()) {
	                if (issu.getActual_return_date() == null) {
	                    issu.setActual_return_date(Date.valueOf(LocalDate.now()));
	                    issueDao.save(issu);
	                    
	                    book.setAvailable_copies(book.getAvailable_copies() + 1);
	                    bookDao.save(book); 
	                   
	                    response.put("status", "success");
	                    response.put("message", cust_name + " successfully returned the book '" + book_name + "' and the amount to be paid is " + issu.getAmt_to_be_paid() + ".");
	                    response.put("amt_to_be_paid", issu.getAmt_to_be_paid());
	                    bookReturned = true;
	                    return response;
	                }
	            }
	        }
	        
	        if (!bookReturned) {
	            response.put("status", "error");
	            response.put("message", "No pending return found for this book and customer.");
	        }
	        
	    } else {
	        response.put("status", "error");
	        response.put("message", "Book or customer not found in service");
	    }
	    
	    return response;
	}

	        
	        


}
