package com.Librarian2.Librarian2.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class IssueBook {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @ManyToOne
    @JoinColumn(name = "cust_id", nullable = false)
    private Customer customer;

    @Temporal(TemporalType.DATE)
    @Column(name = "issue_date", nullable = false)
    private Date issue_date;
    
    private Date actual_return_date;
    
    private Date ideal_return_date;
    
    private Integer amt_to_be_paid;
    																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				
	public IssueBook() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IssueBook(Long id, Books book, Customer customer, Date issue_date, Date actual_return_date,
			Date ideal_return_date, Integer amt_to_be_paid) {
		super();
		this.id = id;
		this.book = book;
		this.customer = customer;
		this.issue_date = issue_date;
		this.actual_return_date = actual_return_date;
		this.ideal_return_date = ideal_return_date;
		this.amt_to_be_paid = amt_to_be_paid;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Books getBook() {
		return book;
	}

	public void setBook(Books book) {
		this.book = book;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(Date issue_date) {
		this.issue_date = issue_date;
	}

	public Date getActual_return_date() {
		return actual_return_date;
	}

	public void setActual_return_date(Date actual_return_date) {
		this.actual_return_date = actual_return_date;
	}

	public Date getIdeal_return_date() {
		return ideal_return_date;
	}

	public void setIdeal_return_date(Date ideal_return_date) {
		this.ideal_return_date = ideal_return_date;
	}

	public Integer getAmt_to_be_paid() {
		return amt_to_be_paid;
	}

	public void setAmt_to_be_paid(Integer amt_to_be_paid) {
		this.amt_to_be_paid = amt_to_be_paid;
	}

	@Override
	public String toString() {
		return "IssueBook [id=" + id + ", book=" + book + ", customer=" + customer + ", issue_date=" + issue_date
				+ ", actual_return_date=" + actual_return_date + ", ideal_return_date=" + ideal_return_date
				+ ", amt_to_be_paid=" + amt_to_be_paid + "]";
	}

	
	
}
