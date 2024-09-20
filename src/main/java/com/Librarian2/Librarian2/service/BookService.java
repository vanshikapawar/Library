package com.Librarian2.Librarian2.service;

import java.util.List;

import com.Librarian2.Librarian2.models.Books;

public interface BookService {
	public List<Books> getBook();
	Books addBook(Books book);
	boolean removeBook(String book_name, String author);
	boolean removeCustomCopies(String book_name, String author, int numCopies);
	List<Books> searchBooks(String query);
}
