package com.olympus.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.olympus.data.vo.TodoVO;

public interface ITodoService {
	 public TodoVO create(TodoVO client);
	 public Page<TodoVO> findTodoByName(String name, Pageable pageable);
	 public Page<TodoVO> findAll(Pageable pageable);
	 public TodoVO findById(Long id);
	 public TodoVO update(TodoVO client);
	 public void delete(Long id);

}
