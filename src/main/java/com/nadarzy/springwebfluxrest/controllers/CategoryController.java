package com.nadarzy.springwebfluxrest.controllers;

import com.nadarzy.springwebfluxrest.model.Category;
import com.nadarzy.springwebfluxrest.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.CATEGORY_URL)
public class CategoryController {

  public static final String CATEGORY_URL = "/api/v1/categories/";
  private final CategoryRepository categoryRepository;

  public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @GetMapping("")
  public Flux<Category> list() {
    return categoryRepository.findAll();
  }

  @GetMapping("{id}")
  public Mono<Category> getById(@PathVariable String id) {
    return categoryRepository.findById(id);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("")
  public Mono<Void> createCategory(@RequestBody Publisher<Category> categoryPublisher) {
    return categoryRepository.saveAll(categoryPublisher).then();
  }

  //  @ResponseStatus(HttpStatus.)
  @PutMapping("{id}")
  public Mono<Category> updateController(@PathVariable String id, @RequestBody Category category) {
    category.setId(id);
    return categoryRepository.save(category);
  }
}
