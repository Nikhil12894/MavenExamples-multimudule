package com.nk.inventoryservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nk.inventoryservice.model.Inventory;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}