package com.Librarian2.Librarian2.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Librarian2.Librarian2.models.Books;

public interface BookDao extends JpaRepository<Books, Long>{
	@Query(value = "SELECT * FROM books WHERE book_name = :bookName", nativeQuery = true)
    Optional<Books> findByName(@Param("bookName") String bookName);
	
	@Query(value = "SELECT * FROM books WHERE book_name = :bookName AND author = :author", nativeQuery = true)
    Optional<Books> findByNameAndAuthor(@Param("bookName") String bookName, @Param("author") String author);
	
	
	@Query("SELECT b FROM Books b WHERE " +
	           "LOWER(b.book_name) LIKE LOWER(CONCAT('%', :query, '%')) " +
	           "OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :query, '%')) " +
	           "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))")
	    List<Books> searchBooks(@Param("query") String query);
	
	@Query(value = "SELECT * FROM books WHERE book_name = :bookName AND author = :author AND genre = :genre", nativeQuery = true)
	Optional<Books> findByBookNameAndAuthorAndGenre(
	        @Param("bookName") String bookName, 
	        @Param("author") String author, 
	        @Param("genre") String genre);
	
	@Query(value = "SELECT * FROM Books WHERE available_copies > 0", nativeQuery = true)
	List<Books> findAvailableBooks();
	
	@Query(value = "SELECT DISTINCT genre FROM books", nativeQuery = true)
    List<String> getAllGenres();

	@Query(value = "SELECT * FROM books WHERE genre = :genre", nativeQuery = true)
    List<Books> findByGenre(@Param("genre") String genre);

	// @Query(value = "SELECT * FROM books WHERE genre IN (:genres)", nativeQuery = true)
    // List<Books> findByGenreIn(@Param("genres") List<String> genres);

	@Query(value = "SELECT * FROM books WHERE genre IN (:genres)", 
           countQuery = "SELECT COUNT(*) FROM books WHERE genre IN (:genres)", 
           nativeQuery = true)
    Page<Books> findByGenreIn(@Param("genres") List<String> genres, Pageable pageable);


	@Query(value = "SELECT COUNT(*) FROM books WHERE genre IN (:genres)", nativeQuery = true)
	long countByGenreIn(@Param("genres") List<String> genres);

	
}
