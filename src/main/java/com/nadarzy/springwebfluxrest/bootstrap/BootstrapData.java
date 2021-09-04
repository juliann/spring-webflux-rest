package com.nadarzy.springwebfluxrest.bootstrap;

import com.nadarzy.springwebfluxrest.model.Category;
import com.nadarzy.springwebfluxrest.model.Vendor;
import com.nadarzy.springwebfluxrest.repository.CategoryRepository;
import com.nadarzy.springwebfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BootstrapData implements CommandLineRunner {

  private final VendorRepository vendorRepository;
  private final CategoryRepository categoryRepository;

  public BootstrapData(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
    this.vendorRepository = vendorRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    if (vendorRepository.count().block() == 0) {
      loadVendors();

      log.debug("loaded Vendor Data");
    }
    if (categoryRepository.count().block() == 0) {
      loadCategories();

      log.debug("loaded Categories Data");
    }
  }

  private void loadCategories() {
    Category category1 = new Category();
    category1.setDescription("Fruits");
    categoryRepository.save(category1).subscribe();

    Category category2 = new Category();
    category2.setDescription("Nuts");
    categoryRepository.save(category2).subscribe();

    Category category3 = new Category();
    category3.setDescription("Bread");
    categoryRepository.save(category3).subscribe();

    Category category4 = new Category();
    category4.setDescription("Eggs");
    categoryRepository.save(category4).subscribe();

    Category category5 = new Category();
    category5.setDescription("Meat");
    categoryRepository.save(category5).subscribe();

    System.out.println("how many cat?: " + categoryRepository.count().block());
  }

  private void loadVendors() {
    Vendor vendor = new Vendor();
    vendor.setFirstName("Shop");
    vendor.setLastName("Seller");
    vendorRepository.save(vendor).subscribe();

    Vendor vendor2 = new Vendor();
    vendor2.setFirstName("Another");
    vendor2.setLastName("Vendor");
    vendorRepository.save(vendor2).subscribe();

    Vendor vendor3 = new Vendor();
    vendor3.setFirstName("Joe");
    vendor3.setLastName("Schmoe");
    vendorRepository.save(vendor3).subscribe();

    System.out.println("how many ven?: " + vendorRepository.count().block());
  }
}
