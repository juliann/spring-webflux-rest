package com.nadarzy.springwebfluxrest.repository;

import com.nadarzy.springwebfluxrest.model.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
