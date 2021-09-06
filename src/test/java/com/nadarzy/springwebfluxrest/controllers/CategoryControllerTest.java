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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
  void testUpdateCategory() {
    Mono<Category> savedCategory = Mono.just(new Category());
    given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(new Category()));

    webTestClient
        .put()
        .uri(CategoryController.CATEGORY_URL + "1")
        .body(savedCategory, Category.class)
        .exchange()
        .expectStatus()
        .isOk();
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

  @Test
  void testPatchCategoryWithChanges() {
    Category dataInDB = new Category();
    dataInDB.setId("123");
    dataInDB.setDescription("this is a test");

    Category dataToPatch = new Category();
    dataToPatch.setId("123");
    dataToPatch.setDescription("this is a better test");

    Mono<Category> savedCategory = Mono.just(dataInDB);
    given(categoryRepository.findById(anyString())).willReturn(Mono.just(dataToPatch));

    given(categoryRepository.save(any(Category.class))).willReturn(savedCategory);

    webTestClient
        .patch()
        .uri(CategoryController.CATEGORY_URL + "1")
        .body(savedCategory, Category.class)
        .exchange()
        .expectStatus()
        .isOk();

    verify(categoryRepository, times(1)).save(any());
  }

  @Test
  void testPatchCategoryNoChanges() {
    Category dataInDB = new Category();
    dataInDB.setId("123");
    dataInDB.setDescription("this is a test");

    Category dataToPatch = new Category();
    dataToPatch.setId("123");
    dataToPatch.setDescription("this is a test");

    Mono<Category> savedCategory = Mono.just(dataInDB);
    given(categoryRepository.findById(anyString())).willReturn(Mono.just(dataToPatch));

    given(categoryRepository.save(any(Category.class))).willReturn(savedCategory);

    webTestClient
        .patch()
        .uri(CategoryController.CATEGORY_URL + "1")
        .body(savedCategory, Category.class)
        .exchange()
        .expectStatus()
        .isOk();

    verify(categoryRepository, times(0)).save(any());
    verify(categoryRepository, times(1)).findById(anyString());
  }
}
