package com.nadarzy.springwebfluxrest.controllers;

import com.nadarzy.springwebfluxrest.model.Vendor;
import com.nadarzy.springwebfluxrest.repository.VendorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.VENDOR_URL)
public class VendorController {

  private final VendorRepository vendorRepository;
  public static final String VENDOR_URL = "/api/v1/vendors/";

  public VendorController(VendorRepository vendorRepository) {
    this.vendorRepository = vendorRepository;
  }

  @GetMapping("")
  public Flux<Vendor> listVendors() {
    return vendorRepository.findAll();
  }

  @GetMapping("{id}")
  public Mono<Vendor> getById(@PathVariable String id) {
    return vendorRepository.findById(id);
  }
}
