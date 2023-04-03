package com.nk.productService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nk.productService.dto.ProductRequest;
import com.nk.productService.dto.ProductResponse;
import com.nk.productService.model.Product;
import com.nk.productService.repository.ProductRepository;
import com.nk.productService.service.ProductService;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceTests {

	@Container
	static MongoDBContainer mongoDBContainer= new MongoDBContainer("mongo:4.4.2");
	
	@Autowired
	private MockMvc mockMvc; 
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductService productService;
	
	@DynamicPropertySource
	static void setProperty(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

	}
	
	
	
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest=getProductRequest();
		String productRequestString=mapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
				Assertions.assertTrue(productRepository.findAll().size()>0);
	}



	private ProductRequest getProductRequest() {
		return ProductRequest.builder().name("iphone 13").description("iphone 13").price(BigDecimal.valueOf(1200)).build();
		
	}
	
	
	@Test
	void shouldFetchProduct() throws Exception {
		productService.createProduct(getProductRequest());
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
				.andExpect(status().isOk())
			      .andExpect(jsonPath("$.[*]").exists())
			      .andExpect(jsonPath("$.[*].id").isNotEmpty());
	}
	
	@Test
	void productTest() throws Exception {
		productService.createProduct(getProductRequest());
		List<ProductResponse> productsResponse= productService.getAllProduct();
		List<Product> products=productsResponse.stream().map(this::getProduct).toList();
		Assertions.assertTrue(products.size()>0);
	}
	
	private Product getProduct(ProductResponse response) {
		return Product.builder().id(response.getId()).name(response.getName()).description(response.getDescription()).price(response.getPrice()).build();
	}
	
	@Test
	void productBuilderTest() {
		String product= Product.builder().id("10").name("iphone 13").description("iphone 13").price(BigDecimal.valueOf(1200)).toString();
		Assertions.assertNotNull(product);
		Assertions.assertTrue(product.contains("iphone 13"));
	}
	
	@Test
	void productModelTest() {
		Product product = new Product();
		product.setId("10");
		product.setName("iphone 13");
		product.setPrice(BigDecimal.valueOf(1200));
		product.setDescription("iphone 13");
		
		Assertions.assertEquals("10", product.getId());
		Assertions.assertEquals("iphone 13", product.getName());
		Assertions.assertEquals("iphone 13", product.getDescription());
		Assertions.assertEquals(BigDecimal.valueOf(1200), product.getPrice());
		Assertions.assertNotNull(product.toString());
	}
	

}
