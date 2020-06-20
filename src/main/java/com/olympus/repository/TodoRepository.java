package com.olympus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.olympus.data.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t WHERE t.name LIKE LOWER(CONCAT ('%', :name, '%'))")
    Page<Todo> findTodoByName(@Param("name") String name, Pageable pageable);
}