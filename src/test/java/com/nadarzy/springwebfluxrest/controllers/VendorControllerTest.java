package com.nadarzy.springwebfluxrest.controllers;

import com.nadarzy.springwebfluxrest.model.Vendor;
import com.nadarzy.springwebfluxrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

  @Test
  void testCreateVendor() {
    Mono<Vendor> toSaveVendor = Mono.just(new Vendor());
    given(vendorRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(new Vendor()));

    webTestClient
        .post()
        .uri(VendorController.VENDOR_URL)
        .body(toSaveVendor, Vendor.class)
        .exchange()
        .expectStatus()
        .isCreated();
  }

  @Test
  void testUpdateVendor() {
    Mono<Vendor> vendor1 = Mono.just(new Vendor());
    given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(new Vendor()));

    webTestClient
        .put()
        .uri(VendorController.VENDOR_URL + "16")
        .body(vendor1, Vendor.class)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  void testPatchVendorWithChanges() {
    Vendor vendorInDB = new Vendor();
    vendorInDB.setFirstName("IKE");
    vendorInDB.setLastName("XXL");

    Vendor vendorToUpdate = new Vendor();
    vendorToUpdate.setLastName("XXL");
    vendorToUpdate.setFirstName("IKEA");

    given(vendorRepository.findById(anyString())).willReturn(Mono.just(vendorInDB));
    given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(vendorToUpdate));

    webTestClient
        .patch()
        .uri(VendorController.VENDOR_URL + "1")
        .body(Mono.just(vendorToUpdate), Vendor.class)
        .exchange()
        .expectStatus()
        .isOk();

    verify(vendorRepository, times(1)).findById(anyString());
    verify(vendorRepository, times(1)).save(any(Vendor.class));
  }

  @Test
  void testPatchVendorNoChanges() {
    Vendor vendorInDB = new Vendor();
    vendorInDB.setFirstName("IKEA");
    vendorInDB.setLastName("XXL");

    Vendor vendorToUpdate = new Vendor();
    vendorToUpdate.setLastName("XXL");
    vendorToUpdate.setFirstName("IKEA");

    given(vendorRepository.findById(anyString())).willReturn(Mono.just(vendorInDB));
    given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(vendorToUpdate));

    webTestClient
        .patch()
        .uri(VendorController.VENDOR_URL + "1")
        .body(Mono.just(vendorToUpdate), Vendor.class)
        .exchange()
        .expectStatus()
        .isOk();

    verify(vendorRepository, times(1)).findById(anyString());
    verify(vendorRepository, times(0)).save(any(Vendor.class));
  }
}
