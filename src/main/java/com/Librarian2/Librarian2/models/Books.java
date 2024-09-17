package com.Librarian2.Librarian2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Books {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long book_id;
	private String book_name;
	private String genre;
	private String author;
	private Integer total_stock;
	private Integer available_copies;
	public Books() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Books(Long book_id, String book_name, String genre, String author, Integer total_stock,
			Integer available_copies) {
		super();
		this.book_id = book_id;
		this.book_name = book_name;
		this.genre = genre;
		this.author = author;
		this.total_stock = total_stock;
		this.available_copies = available_copies;
	}
	public Long getBook_id() {
		return book_id;
	}
	public void setBook_id(Long book_id) {
		this.book_id = book_id;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getTotal_stock() {
		return total_stock;
	}
	public void setTotal_stock(Integer total_stock) {
		this.total_stock = total_stock;
	}
	public Integer getAvailable_copies() {
		return available_copies;
	}
	public void setAvailable_copies(Integer available_copies) {
		this.available_copies = available_copies;
	}
	@Override
	public String toString() {
		return "Books [book_id=" + book_id + ", book_name=" + book_name + ", genre=" + genre + ", author=" + author
				+ ", total_stock=" + total_stock + ", available_copies=" + available_copies + "]";
	}
	
	
	
}
