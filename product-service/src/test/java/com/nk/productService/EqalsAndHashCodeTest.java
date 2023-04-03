package com.nk.productService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.nk.productService.dto.ProductRequest;
import com.nk.productService.dto.ProductResponse;
import com.nk.productService.model.Product;

import nl.jqno.equalsverifier.EqualsVerifier;

@SpringBootTest
class EqalsAndHashCodeTest {
	
	@Test
    void equalsContractTest() {
        EqualsVerifier.simple().forClass(Product.class).verify();
        EqualsVerifier.simple().forClass(ProductResponse.class).verify();
        EqualsVerifier.simple().forClass(ProductRequest.class).verify();
    }
}
