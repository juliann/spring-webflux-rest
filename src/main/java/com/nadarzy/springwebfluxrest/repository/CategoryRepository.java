package com.nadarzy.springwebfluxrest.repository;

import com.nadarzy.springwebfluxrest.model.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category,  String> {
}
