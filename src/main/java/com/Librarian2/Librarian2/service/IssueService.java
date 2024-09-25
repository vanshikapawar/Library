package com.Librarian2.Librarian2.service;

import java.util.List;
import java.util.Map;

import com.Librarian2.Librarian2.models.IssueBook;

public interface IssueService {
	public List<IssueBook> getIssuedBook();
	IssueBook addIssueBook(IssueBook issue);
	boolean issuedBook(String book_name, String cust_name, String email);
	public Map<String, Object> returnBook(String book_name, String cust_name, String email);
	List<Map<String, Object>> findIssuedBooksByEmail(String email);
	Map<String, Object> reissueBook(String book_name, String cust_name, String email);
}
