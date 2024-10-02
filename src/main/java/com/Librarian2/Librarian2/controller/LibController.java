package com.Librarian2.Librarian2.controller;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.Librarian2.Librarian2.models.Books;
import com.Librarian2.Librarian2.models.Customer;
import com.Librarian2.Librarian2.models.Events;
import com.Librarian2.Librarian2.models.IssueBook;
import com.Librarian2.Librarian2.service.BookService;
import com.Librarian2.Librarian2.service.CustomerService;
import com.Librarian2.Librarian2.service.EventService;
import com.Librarian2.Librarian2.service.ExcelService;
import com.Librarian2.Librarian2.service.IssueService;

@RestController
public class LibController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ExcelService exService;
	
	@GetMapping("/home")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("HomePage.html");
		return modelAndView;
	}
	
	@GetMapping("/books")
	public Map<String, Object> getBooks(
		@RequestParam(value="pageNo", defaultValue="0", required=false) Integer pageNo,
		@RequestParam(value="pageSize", defaultValue="10", required=false) Integer pageSize) {
		
		List<Books> books = bookService.getBook(pageNo, pageSize);
		long totalBooks = bookService.countTotalBooks();
		int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

		Map<String, Object> response = new HashMap<>();
		response.put("books", books);
		response.put("totalPages", totalPages);

		return response;
	}
	
	
	@GetMapping("/genre")
	public Map<String, Object> getBooksByGenres(
			@RequestParam List<String> genre,
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
				List<Books> books = bookService.getBooksByGenres(genre, pageNo, pageSize);
				long totalBooks = bookService.countTotalBooksByGenres(genre);
		int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

		Map<String, Object> response = new HashMap<>();
		response.put("books", books);
		response.put("totalPages", totalPages);

		return response;  // Pass the list of genres and pagination parameters
	}

	@GetMapping("/events")
	public List<Events> getEvents(){
		return this.eventService.getEvents();
	}
	
	
	@PostMapping("/books")
	public ResponseEntity<Books> addBooks(@RequestBody Books book) {
		Books addedBook =this.bookService.addBook(book);
		return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
	}
	
	// Method to add copies to an existing book
    @PostMapping("/addBookCopies")
    public ResponseEntity<String> addBookCopies(@RequestBody Books book) {
        try {
            boolean updated = bookService.addBookCopies(book);
            if (updated) {
                return new ResponseEntity<>("Book copies updated successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Book not found to update.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	
	@DeleteMapping("/books")
	public ResponseEntity<Void> deleteBook(@RequestParam("book_name") String bookName, 
	                                       @RequestParam("author") String author, 
	                                       @RequestParam(value = "customRemove", required = false) Boolean customRemove, 
	                                       @RequestParam(value = "numCopies", required = false) Integer numCopies) {
	    boolean deleted;
	    
	    if (Boolean.TRUE.equals(customRemove) && numCopies != null) {
	        // Remove a custom number of copies
	        deleted = this.bookService.removeCustomCopies(bookName, author, numCopies);
	    } else {
	        // Remove the book entirely
	        deleted = this.bookService.removeBook(bookName, author);
	    }
	    
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
	
	@PostMapping("/reissuebook")
	public ResponseEntity<String> reissueBook(@RequestParam String book_name, 
	                                          @RequestParam String cust_name, 
	                                          @RequestParam String email) {
	    Map<String, Object> response = issueService.reissueBook(book_name, cust_name, email);
	    if (response.get("status").equals("success")) {
	        return ResponseEntity.ok("Success");
	    } else {
	        return ResponseEntity.status((Integer) response.get("status_code")).body(response.get("message").toString());
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
	
	@GetMapping("/byEmail")
    public ResponseEntity<?> getCustomerByEmail(@RequestParam String email) {
        Customer customer = customerService.findByEmail(email);
        if (customer != null) {
            return ResponseEntity.ok(Map.of("name", customer.getCust_name()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Customer not found"));
        }
    }
	
	
	 @PostMapping("/eventAdd")
	    public String addEvent(@RequestBody Events event) {
	        try {
	            eventService.addEvent(event);
	            return "success"; // Redirect to the events list or a success page
	        } catch (Exception e) {
	            return "error"; // Redirect to an error page or handle error
	        }
	    }
	 
	 @GetMapping("/searchCust")
	 public List<Map<String, Object>> getIssuedBooksByEmail(@RequestParam String email) {
	     return issueService.findIssuedBooksByEmail(email);
	 }
	 
	 @GetMapping("/getAmountToPay")
	    public ResponseEntity<?> getAmountToPay(@RequestParam String email, @RequestParam String book_name) {
	        try {
	            double amountToPay = issueService.calculateAmountToPay(email, book_name);
	            return ResponseEntity.ok(Map.of("amount", amountToPay));
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch amount: " + e.getMessage()));
	        }
	    }
	 
	 @RequestMapping("/excel")
		public ResponseEntity<Resource> download() throws IOException{
			String filename = "availableBooks.xlsx";
			ByteArrayInputStream actualData = exService.getActualData();
			InputStreamResource file= new InputStreamResource(actualData);

			ResponseEntity<Resource> body =ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+filename)
			.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
			
			return body;
		}
	 
	 @GetMapping("/genres")
		public List<String> getGenres() {
			return bookService.getGenres(); // Implement this method in your service
		}

		// @GetMapping("/genre")
		// public List<Books> getBooksByGenres(@RequestParam List<String> genre) {
		// 	return bookService.getBooksByGenres(genre); // Pass the list of genres
		// }



		@GetMapping("/totalCount")
    public ResponseEntity<Map<String, Long>> getTotalBooksCount() {
        long totalBooks = bookService.countTotalBooks();
        Map<String, Long> response = new HashMap<>();
        response.put("totalBooks", totalBooks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@GetMapping("/emailSuggestions")
public ResponseEntity<List<String>> getEmailSuggestions(@RequestParam String query) {
    List<String> suggestions = customerService.getEmailSuggestions(query);
    return ResponseEntity.ok(suggestions);
}

@GetMapping("/bookDetails")
public ResponseEntity<Map<String, String>> getBookDetails(@RequestParam String title) {
    Books book = bookService.findByTitle(title);
    if (book != null) {
        Map<String, String> details = new HashMap<>();
        details.put("author", book.getAuthor());
        details.put("genre", book.getGenre());
		details.put("available_copies", String.valueOf(book.getAvailable_copies()));
        return ResponseEntity.ok(details);
    } else {
        return ResponseEntity.notFound().build();
    }
}

@GetMapping("/bookTitleSuggestions")
public ResponseEntity<List<String>> getBookTitleSuggestions(@RequestParam String query) {
    List<String> suggestions = bookService.getBookTitleSuggestions(query);
    return ResponseEntity.ok(suggestions);
}
}