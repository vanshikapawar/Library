package com.Librarian2.Librarian2.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Librarian2.Librarian2.models.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long>{
	
	@Query(value = "SELECT * FROM customer WHERE cust_name = :custName AND email = :email", nativeQuery = true)
	Optional<Customer> findByNameAndEmail(@Param("custName") String custName, @Param("email") String email);

	@Query(value = "SELECT * FROM customer WHERE email = :email", nativeQuery = true)
	Optional<Customer> findByEmail(@Param("email") String email);
	
}
