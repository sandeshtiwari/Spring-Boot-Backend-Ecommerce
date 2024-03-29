package com.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dto.OrderItemRequestDto;
import com.backend.dto.OrderRequestDto;
import com.backend.dto.OrderResponseDto;
import com.backend.dto.ShippingAddressDto;
import com.backend.exceptions.ProductNotFoundException;
import com.backend.exceptions.UserNotFoundException;
import com.backend.model.OrderEntity;
import com.backend.model.OrderItemsEntity;
import com.backend.model.ProductEntity;
import com.backend.model.ShippingAddressEntity;
import com.backend.model.UserEntity;
import com.backend.repository.OrderItemsRepository;
import com.backend.repository.OrderRepository;
import com.backend.repository.ProductRepository;
import com.backend.repository.ShippingAddressRepository;
import com.backend.repository.UserRepository;
import com.backend.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        // Validate user
        UserEntity user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Validate products and calculate prices
        List<OrderItemsEntity> orderItemsEntities = new ArrayList<>();
        long itemsPrice = 0;
        for (OrderItemRequestDto itemRequest : orderRequestDto.getItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            OrderItemsEntity orderItem = new OrderItemsEntity();
            orderItem.setProductEntity(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice() * itemRequest.getQuantity());
            itemsPrice += orderItem.getPrice();
            // Note: Set `orderEntity` for `orderItem` after order creation to avoid null
            // reference
            orderItemsEntities.add(orderItem);
        }

        // Create and save the shipping address
        ShippingAddressEntity shippingAddress = new ShippingAddressEntity(
                0, // Assuming ID is auto-generated
                orderRequestDto.getShippingAddress().getAddress(),
                orderRequestDto.getShippingAddress().getCity(),
                orderRequestDto.getShippingAddress().getPostalCode(),
                orderRequestDto.getShippingAddress().getCountry());
        shippingAddress = shippingAddressRepository.save(shippingAddress);

        // Create and save the order
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setItemsPrice(itemsPrice);
        order.setTaxPrice(calculateTax(itemsPrice));
        // order.setShippingPrice(calculateShippingFee(shippingAddress));
        order.setShippingPrice(calculateShippingFee(itemsPrice));
        order.setTotalPrice(order.getItemsPrice() + order.getTaxPrice() + order.getShippingPrice());
        order.setPaymentMethod(orderRequestDto.getPaymentMethod());
        order.setPaid(false);
        order.setDelivered(false);
        order.setCreatedAt(new Date());
        OrderEntity savedOrder = orderRepository.save(order);

        // Save order items with reference to the order
        orderItemsEntities.forEach(item -> {
            item.setOrderEntity(savedOrder);
            orderItemsRepository.save(item);
        });

        return mapToResponseDto(savedOrder);
    }

    private OrderResponseDto mapToResponseDto(OrderEntity orderEntity) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setItemsPrice(orderEntity.getItemsPrice());
        orderResponseDto.setOrderId(orderEntity.getOrderId());
        System.err.println("Getting order items entity ************************");
        System.out.println(orderEntity.getOrderItemsEntity());
        orderResponseDto.setOrderItemsEntity(orderEntity.getOrderItemsEntity());
        orderResponseDto.setPaymentMethod(orderEntity.getPaymentMethod());
        orderResponseDto.setUserId(orderEntity.getUser().getId());
        orderResponseDto.setShippingAddressDto(mapToShippingDto(orderEntity.getShippingAddress()));
        orderResponseDto.setShippingPrice(orderEntity.getShippingPrice());
        orderResponseDto.setTaxPrice(orderEntity.getTaxPrice());
        orderResponseDto.setTotalPrice(orderEntity.getTotalPrice());

        return orderResponseDto;
    }

    private ShippingAddressDto mapToShippingDto(ShippingAddressEntity shippingAddressEntity) {
        ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
        shippingAddressDto.setAddress(shippingAddressEntity.getAddress());
        shippingAddressDto.setCity(shippingAddressEntity.getCity());
        shippingAddressDto.setPostalCode(shippingAddressEntity.getPostalCode());
        shippingAddressDto.setCountry(shippingAddressEntity.getCountry());
        return shippingAddressDto;
    }

    private long calculateTax(long itemsPrice) {
        return (long) (itemsPrice * 0.15); // 10% tax
    }

    // private long calculateShippingFee(ShippingAddressEntity shippingAddress, long
    // itemsPrice) {
    // // shipping fee calculation
    // return 500; // flat 500 shipping fee
    // }
    private long calculateShippingFee(long itemsPrice) {
        // shipping fee calculation
        return itemsPrice > 100 ? 10 : 0;
    }
}