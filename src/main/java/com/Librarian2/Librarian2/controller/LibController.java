package com.Librarian2.Librarian2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.Librarian2.Librarian2.models.Books;
import com.Librarian2.Librarian2.models.Customer;
import com.Librarian2.Librarian2.models.IssueBook;
import com.Librarian2.Librarian2.service.BookService;
import com.Librarian2.Librarian2.service.CustomerService;
import com.Librarian2.Librarian2.service.IssueService;

@RestController
public class LibController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private IssueService issueService;
	
	@GetMapping("/home")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("HomePage.html");
		return modelAndView;
	}
	
	@GetMapping("/books")
	public List<Books> getBooks(){
		return this.bookService.getBook();
	}
	
	@PostMapping("/books")
	public ResponseEntity<Books> addBooks(@RequestBody Books book) {
		Books addedBook =this.bookService.addBook(book);
		return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/books")
    public ResponseEntity<Void> deleteBook(@RequestParam("book_name") String book_name, @RequestParam("author") String author) {
        boolean deleted = this.bookService.removeBook(book_name, author);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	@GetMapping("/customer")
	public List<Customer> getCustomers(){
		return this.customerService.getCustomer();
	}

	@PostMapping("/customer")
	public ResponseEntity<Customer> addCustomers(@RequestBody Customer customer) {
	    Customer added = this.customerService.addCustomer(customer);
	    return new ResponseEntity<>(added, HttpStatus.CREATED);
	}
	
	@GetMapping("/issuebook")
	public List<IssueBook> getIssued(){
		return this.issueService.getIssuedBook();
	}
	
	@PostMapping("/issuebook")
    public ResponseEntity<String> issueBook(
            @RequestParam("book_name") String book_name,
            @RequestParam("cust_name") String cust_name,
            @RequestParam("email") String email) {

        boolean issued = issueService.issuedBook(book_name, cust_name, email);

        if (issued) {
            return ResponseEntity.ok("Book issued successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to issue book. Check book availability or customer ID.");
        }
    }
	
	@PostMapping("/returnbook")
    public ResponseEntity<Map<String, Object>> returnABook(
        @RequestParam String book_name, 
        @RequestParam String cust_name, 
        @RequestParam String email) {

        Map<String, Object> response = issueService.returnBook(book_name, cust_name, email);
        String message = (String) response.get("message");

        if (message.equals("Book returned successfully.")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
	
	@GetMapping("/search")
    public List<Books> searchBooks(@RequestParam String query) {
        return bookService.searchBooks(query);
    }

}
