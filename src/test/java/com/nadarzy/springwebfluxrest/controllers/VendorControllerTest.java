package com.nadarzy.springwebfluxrest.controllers;

import com.nadarzy.springwebfluxrest.model.Vendor;
import com.nadarzy.springwebfluxrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

// @ExtendWith(MockitoExtension.class)
class VendorControllerTest {

  @Mock VendorRepository vendorRepository;

  VendorController vendorController;
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    //    MockitoExtension vendorRepository = Mockito.mock(VendorRepository.class);
    MockitoAnnotations.openMocks(this);
    vendorController = new VendorController(vendorRepository);
    webTestClient = WebTestClient.bindToController(vendorController).build();
  }

  @Test
  void listVendors() {
    Vendor vendor1 = new Vendor();
    Vendor vendor2 = new Vendor();
    Vendor vendor3 = new Vendor();
    given(vendorController.listVendors()).willReturn(Flux.just(vendor1, vendor2, vendor3));

    webTestClient
        .get()
        .uri(VendorController.VENDOR_URL)
        .exchange()
        .expectBodyList(Vendor.class)
        .hasSize(3);
  }

  @Test
  void getById() {
    Vendor vendor1 = new Vendor();
    given(vendorController.getById(anyString())).willReturn(Mono.just(vendor1));

    webTestClient.get().uri(VendorController.VENDOR_URL + "1").exchange().expectBody(Vendor.class);
  }
}
