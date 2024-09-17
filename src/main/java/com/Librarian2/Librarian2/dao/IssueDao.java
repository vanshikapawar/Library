package com.Librarian2.Librarian2.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Librarian2.Librarian2.models.IssueBook;

public interface IssueDao extends JpaRepository<IssueBook, Long>{
	@Query(value = "SELECT * FROM issue_book WHERE book_id = :bookId AND actual_return_date IS NULL", nativeQuery = true)
    List<IssueBook> findActiveIssuesByBookId(@Param("bookId") Long bookId);
}
