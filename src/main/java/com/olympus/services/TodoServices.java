package com.olympus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.olympus.converter.DozerConverter;
import com.olympus.data.model.Todo;
import com.olympus.data.vo.TodoVO;
import com.olympus.exception.ResourceNotFoundException;
import com.olympus.repository.TodoRepository;
import com.olympus.util.MessagesUtil;

@Service
public class TodoServices {

    @Autowired
    TodoRepository repository;

    public TodoVO create(TodoVO client) {
        Todo entity = DozerConverter.parseObject(client, Todo.class);
        return DozerConverter.parseObject(repository.save(entity), TodoVO.class);
    }

    public Page<TodoVO> findTodoByName(String name, Pageable pageable) {
        Page<Todo> page = repository.findTodoByName(name, pageable);
        return page.map(this::convertToTodoVO);
    }

    public Page<TodoVO> findAll(Pageable pageable) {
        Page<Todo> page = repository.findAll(pageable);
        return page.map(this::convertToTodoVO);
    }

    private TodoVO convertToTodoVO(Todo entity) {
        return DozerConverter.parseObject(entity, TodoVO.class);
    }

    public TodoVO findById(Long id) {
    	Todo entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));
        return DozerConverter.parseObject(entity, TodoVO.class);
    }

    public TodoVO update(TodoVO client) {
        Todo entity = repository.findById(client.getKey())
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));

        entity.setName(client.getName());
        entity.setDescription(client.getDescription());

        return DozerConverter.parseObject(repository.save(entity), TodoVO.class);
    }

    public void delete(Long id) {
    	Todo entity = null;
    	entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));
        repository.delete(entity);
    }

}
