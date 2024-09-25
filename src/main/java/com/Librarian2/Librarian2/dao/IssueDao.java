package com.Librarian2.Librarian2.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Librarian2.Librarian2.models.IssueBook;

public interface IssueDao extends JpaRepository<IssueBook, Long>{
	@Query(value = "SELECT * FROM issue_book WHERE book_id = :bookId AND actual_return_date IS NULL", nativeQuery = true)
    List<IssueBook> findActiveIssuesByBookId(@Param("bookId") Long bookId);
	
	@Query(value = "SELECT b.book_name, ib.issue_date FROM issue_book ib JOIN customer c ON ib.cust_id = c.cust_id JOIN books b ON ib.book_id = b.book_id WHERE c.email = :email AND ib.actual_return_date IS NULL", nativeQuery = true)
	List<Object[]> findActiveIssuesByCustomerEmail(@Param("email") String email);

	@Query(value = "SELECT ib.* FROM issue_book ib " +
            "JOIN books b ON ib.book_id = b.book_id " +
            "JOIN customer c ON ib.cust_id = c.cust_id " +
            "WHERE b.book_name = :bookName " +
            "AND c.email = :email " +
            "AND ib.actual_return_date IS NULL", nativeQuery = true)
IssueBook findByIssuedBookForCust(@Param("bookName") String bookName, 
                               @Param("email") String email);


}
