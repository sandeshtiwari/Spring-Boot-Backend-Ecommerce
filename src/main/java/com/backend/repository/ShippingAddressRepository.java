package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.ShippingAddressEntity;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddressEntity, Integer> {

}
