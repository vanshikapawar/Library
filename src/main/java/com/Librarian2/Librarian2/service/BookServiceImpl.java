package com.Librarian2.Librarian2.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Librarian2.Librarian2.dao.BookDao;
import com.Librarian2.Librarian2.dao.IssueDao;
import com.Librarian2.Librarian2.models.Books;
import com.Librarian2.Librarian2.models.IssueBook;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	private BookDao bookDao;
	
	@Autowired
	private IssueDao issuedDao;
	

	@Override
	public long countTotalBooks() {
        return bookDao.count();
    }

	@Override
public long countTotalBooksByGenres(List<String> genres) {
    return bookDao.countByGenreIn(genres);
}

	@Override
	public List<Books> getBook(Integer pageNo, Integer pageSize) {
		// int pageSize = 2;
		// int pageNumber = 1;

		Pageable p = PageRequest.of(pageNo, pageSize);
		Page<Books> pageBook = bookDao.findAll(p);
		List<Books> allbook = pageBook.getContent();
		return allbook;
	}

	@Override
	public List<Books> getBooksByGenres(List<String> genres, Integer pageNo, Integer pageSize){
		Pageable p = PageRequest.of(pageNo, pageSize);
		Page<Books> pageBooks = bookDao.findByGenreIn(genres, p);
		List<Books> allbook = pageBooks.getContent();
		return allbook;
	}


	@Override
	public Books addBook(Books book) {
		bookDao.save(book);
		return book;
	}

	@Override
	public boolean removeBook(String book_name, String author) {
		Optional<Books> optionalBook = bookDao.findByNameAndAuthor(book_name, author);
		if (optionalBook.isPresent()) {
            Books book = optionalBook.get();
          
            List<IssueBook> issuedBooks = issuedDao.findActiveIssuesByBookId(book.getBook_id());
            
            if (issuedBooks.isEmpty()) {
                bookDao.delete(book);
                return true;
            } else {
              
                int issuedCopies = issuedBooks.size();
                
                book.setTotal_stock(issuedCopies);
                book.setAvailable_copies(0); 
                
                bookDao.save(book);
                return true;  
            }
        } else {
        	throw new NoSuchElementException("Book not found with name: " + book_name + " by " + author);
        }
	}
	
	@Override
	public boolean removeCustomCopies(String book_name, String author, int numCopies) {
	    Optional<Books> optionalBook = bookDao.findByNameAndAuthor(book_name, author);
	    if (optionalBook.isPresent()) {
	        Books book = optionalBook.get();

	        int availableCopies = book.getAvailable_copies();
	        int totalStock = book.getTotal_stock();

	        // Check if the number of copies to be removed is valid
	        if (numCopies > availableCopies || numCopies > totalStock) {
	            throw new IllegalArgumentException("Cannot remove more copies than available in stock.");
	        }

	        // Update available copies and total stock
	        book.setAvailable_copies(availableCopies - numCopies);
	        book.setTotal_stock(totalStock - numCopies);

	        bookDao.save(book);
	        return true;
	    } else {
	        throw new NoSuchElementException("Book not found with name: " + book_name + " by " + author);
	    }
	}


	@Override
	public List<Books> searchBooks(String query) {
		return bookDao.searchBooks(query);
	}

	@Override
	public boolean addBookCopies(Books book) {
	    Optional<Books> existingBookOpt = bookDao.findByBookNameAndAuthorAndGenre(book.getBook_name(), book.getAuthor(), book.getGenre());
	    if (existingBookOpt.isPresent()) {
	        Books existingBook = existingBookOpt.get();
	        // Update available copies and total stock
	        existingBook.setAvailable_copies(existingBook.getAvailable_copies() + book.getAvailable_copies());
	        existingBook.setTotal_stock(existingBook.getTotal_stock() + book.getTotal_stock());

	        // Save the updated book back to the database
	        bookDao.save(existingBook);
	        return true; // Indicate that the update was successful
	    } else {
	        return false; // Indicate that the book was not found
	    }
	}
	
	@Override
	public List<String> getGenres() {
		return bookDao.getAllGenres();
	}
	
	// @Override
	// public List<Books> getBooksByGenres(List<String> genres) {
    //     return bookDao.findByGenreIn(genres); // Use 'findByGenreIn' to fetch books
    // }

	@Override
public Books findByTitle(String title) {
    return bookDao.findByName(title).orElse(null);
}
		
}
