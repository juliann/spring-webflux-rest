package com.nadarzy.springwebfluxrest.controllers;

import com.nadarzy.springwebfluxrest.model.Vendor;
import com.nadarzy.springwebfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("")
  public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorPublisher) {
    return vendorRepository.saveAll(vendorPublisher).then();
  }

  @PutMapping("{id}")
  public Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
    vendor.setId(id);
    return vendorRepository.save(vendor);
  }

  @PatchMapping("{id}")
  public Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
    Mono<Vendor> vendorToPatch = vendorRepository.findById(id);
    Vendor vendor1 = vendorToPatch.block();
    if (!vendor1.getFirstName().equals(vendor.getFirstName())) {
      vendor1.setFirstName(vendor.getFirstName());
      return vendorRepository.save(vendor1);
    }
    return vendorToPatch;
  }
}
