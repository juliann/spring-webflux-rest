package com.nadarzy.springwebfluxrest.controllers;

import com.nadarzy.springwebfluxrest.model.Category;
import com.nadarzy.springwebfluxrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CategoryControllerTest {

  private CategoryController categoryController;
  private WebTestClient webTestClient;
  private CategoryRepository categoryRepository;

  @BeforeEach
  void setUp() {
    categoryRepository = Mockito.mock(CategoryRepository.class);
    categoryController = new CategoryController(categoryRepository);
    webTestClient = WebTestClient.bindToController(categoryController).build();
  }

  @Test
  void list() {
    Category category1 = new Category();
    category1.setDescription("Cat1");

    Category category2 = new Category();
    category2.setDescription("Cat2");

    given(categoryRepository.findAll()).willReturn(Flux.just(category1, category2));

    webTestClient
        .get()
        .uri(CategoryController.CATEGORY_URL)
        .exchange()
        .expectBodyList(Category.class)
        .hasSize(2);
  }

  @Test
  void getById() {
    Category category1 = new Category();
    category1.setDescription("Cat1");

    given(categoryRepository.findById("123")).willReturn(Mono.just(category1));

    webTestClient
        .get()
        .uri(CategoryController.CATEGORY_URL + "123")
        .exchange()
        .expectBody(Category.class);
  }

  @Test
  void testCreateCategory() {
    Category category1 = new Category();
    category1.setDescription("Cat1");
    given(categoryRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(category1));
    Mono<Category> categoryToSaveMono = Mono.just(category1);

    webTestClient
        .post()
        .uri(CategoryController.CATEGORY_URL)
        .body(categoryToSaveMono, Category.class)
        .exchange()
        .expectStatus()
        .isCreated();
  }
}
