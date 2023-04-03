package com.nk.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nk.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
