package com.Librarian2.Librarian2.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Books> getBook() {
		return bookDao.findAll();
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
	public List<Books> searchBooks(String query) {
		return bookDao.searchBooks(query);
	}
		
}
