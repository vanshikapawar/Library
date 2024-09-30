package com.Librarian2.Librarian2.service;

import java.util.List;

import com.Librarian2.Librarian2.models.Books;

public interface BookService {
	//Page<Books> getBookPage(Integer pageNo, Integer pageSize);
	public List<Books> getBook(Integer pageNo, Integer pageSize);
	long countTotalBooks();
	Books addBook(Books book);
	boolean addBookCopies(Books book);
	boolean removeBook(String book_name, String author);
	boolean removeCustomCopies(String book_name, String author, int numCopies);
	List<Books> searchBooks(String query);
	List<String> getGenres();
	long countTotalBooksByGenres(List<String> genres);
	List<Books> getBooksByGenres(List<String> genres, Integer pageNo, Integer pageSize);
	Books findByTitle(String title);
}
