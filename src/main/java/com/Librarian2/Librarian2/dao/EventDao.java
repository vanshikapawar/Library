package com.Librarian2.Librarian2.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Librarian2.Librarian2.models.Events;


public interface EventDao extends JpaRepository<Events, Long>{

}
