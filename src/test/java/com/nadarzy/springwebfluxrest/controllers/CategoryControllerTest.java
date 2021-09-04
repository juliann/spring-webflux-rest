package com.nadarzy.springwebfluxrest.controllers;

import com.nadarzy.springwebfluxrest.model.Category;
import com.nadarzy.springwebfluxrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    BDDMockito.given(categoryRepository.findAll()).willReturn(Flux.just(category1, category2));

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

    BDDMockito.given(categoryRepository.findById("123")).willReturn(Mono.just(category1));

    webTestClient
        .get()
        .uri(CategoryController.CATEGORY_URL + "123")
        .exchange()
        .expectBody(Category.class);
  }
}
