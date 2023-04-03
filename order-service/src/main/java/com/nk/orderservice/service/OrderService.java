package com.nk.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.nk.orderservice.dto.InventoryResponse;
import com.nk.orderservice.dto.OrderLineItemsDto;
import com.nk.orderservice.dto.OrderRequest;
import com.nk.orderservice.model.Order;
import com.nk.orderservice.model.OrderLineItems;
import com.nk.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;

	@Transactional
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());

		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDto().stream()
				.map(this::mapToOrderLinesItems).toList();

		order.setOrderLineItemsList(orderLineItems);

		List<String> skuCodes = orderRequest.getOrderLineItemsDto().stream()
				.map(OrderLineItemsDto::getSkuCode).toList();

		// Make Call To InventoryService and check if oredrLineItems are in stock?

		InventoryResponse[] inventoryResponseArray=webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve().bodyToMono(InventoryResponse[].class).block();
		boolean allProductInStock=Arrays.stream(inventoryResponseArray)
				.allMatch(InventoryResponse::isInStock);
		if(allProductInStock) {
			orderRepository.save(order);
			log.info("order {} was placed!", order.getOrderNumber());
		}else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
		
	}

	private OrderLineItems mapToOrderLinesItems(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
}
