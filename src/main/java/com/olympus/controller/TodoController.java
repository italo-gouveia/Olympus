package com.olympus.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olympus.data.vo.TodoVO;
import com.olympus.services.TodoServices;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "TodoEndpoint")
@RestController
@RequestMapping("/api/todo")
public class TodoController {

	@Autowired
	private TodoServices service;

    @Autowired
    private PagedResourcesAssembler<TodoVO> assembler;

    private static final String DESC_CONST = "desc";

	@ApiOperation(value = "Return a simples String of test of version API Response defined by header: X-API-VERSION" )
	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=2")
	public String findAll() {
		return "Hello API V2";
	}

    @ApiOperation(value = "List all Todos" )
    @GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
    public ResponseEntity<?> findAll(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction) {

        Direction sortDirection = DESC_CONST.equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        Page<TodoVO> todos =  service.findAll(pageable);
        todos
                .stream()
                .forEach(p -> p.add(
                        linkTo(methodOn(TodoController.class).findById(p.getKey())).withSelfRel()
                        )
                );

        PagedModel<?> resources = assembler.toModel(todos);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Find Todos by name" )
    @GetMapping(value = "/findTodoByName/{name}", produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
    public ResponseEntity<?> findPersonByName(
            @PathVariable("name") String name,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction) {

        Direction sortDirection = DESC_CONST.equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        Page<TodoVO> todos =  service.findTodoByName(name, pageable);
        todos
                .stream()
                .forEach(p -> p.add(
                        linkTo(methodOn(TodoController.class).findById(p.getKey())).withSelfRel()
                        )
                );

        PagedModel<?> resources = assembler.toModel(todos);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Find a Todo by ID" )
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public TodoVO findById(@PathVariable("id") Long id) {
		TodoVO todoVO = service.findById(id);
		todoVO.add(linkTo(methodOn(TodoController.class).findById(id)).withSelfRel());
		return todoVO;
	}

	@ApiOperation(value = "Create a new Todo")
	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
			consumes = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public TodoVO create(@RequestBody TodoVO todo) {
		TodoVO todoVO = service.create(todo);
		todoVO.add(linkTo(methodOn(TodoController.class).findById(todoVO.getKey())).withSelfRel());
		return todoVO;
	}

	@ApiOperation(value = "Update a Todo by ID")
	@PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
			consumes = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public TodoVO update(@RequestBody TodoVO todo) {
		TodoVO todoVO = service.update(todo);
		todoVO.add(linkTo(methodOn(TodoController.class).findById(todoVO.getKey())).withSelfRel());
		return todoVO;
	}

	@ApiOperation(value = "Delete a Todo by ID")
	@DeleteMapping(value="/{id}", headers = "X-API-VERSION=1")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

}
