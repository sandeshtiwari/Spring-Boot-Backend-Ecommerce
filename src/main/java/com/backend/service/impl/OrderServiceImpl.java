package com.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dto.OrderItemRequestDto;
import com.backend.dto.OrderItemResponseDto;
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
    public OrderEntity createOrder(OrderRequestDto orderRequestDto) {
        // Validate user
        // UserEntity user = userRepository.findById(orderRequestDto.getUserId())
        // .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserEntity user = userRepository.findByUsername(orderRequestDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Validate products and calculate prices
        List<OrderItemsEntity> orderItemsEntities = new ArrayList<>();
        long itemsPrice = 0;
        for (OrderItemRequestDto itemRequest : orderRequestDto.getItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            OrderItemsEntity orderItem = new OrderItemsEntity();
            orderItem.setProductEntity(product);
            orderItem.setImage(product.getImage());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice() * itemRequest.getQuantity());
            itemsPrice += orderItem.getPrice();
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

        // Save order items with reference to the order
        orderItemsEntities.forEach(item -> {
            item.setOrderEntity(order);
            orderItemsRepository.save(item);
        });
        order.setOrderItemsEntity(orderItemsEntities);
        OrderEntity savedOrder = orderRepository.save(order);

        return savedOrder;
    }

    private OrderResponseDto mapToResponseDto(OrderEntity orderEntity) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setItemsPrice(orderEntity.getItemsPrice());
        orderResponseDto.setOrderId(orderEntity.getOrderId());
        // System.err.println("1 Getting order items entity ************************");
        // System.out.println(orderEntity.getOrderItemsEntity());
        orderResponseDto.setOrderItems(orderEntity.getOrderItemsEntity().stream()
                .map(o -> mapToOrderItemResponseDto(o)).collect(Collectors.toList()));
        // .stream().map(p -> mapToDto(p)).collect(Collectors.toList())
        // System.err.println("2 Getting order items entity ************************");
        orderResponseDto.setPaymentMethod(orderEntity.getPaymentMethod());
        // System.err.println("3 Getting order items entity ************************");
        orderResponseDto.setUserId(orderEntity.getUser().getId());
        // System.err.println("4 Getting order items entity ************************");
        orderResponseDto.setShippingAddress(mapToShippingDto(orderEntity.getShippingAddress()));
        // System.err.println("5 Getting order items entity ************************");
        orderResponseDto.setShippingPrice(orderEntity.getShippingPrice());
        // System.err.println("6 Getting order items entity ************************");
        orderResponseDto.setTaxPrice(orderEntity.getTaxPrice());
        // System.err.println("7 Getting order items entity ************************");
        orderResponseDto.setTotalPrice(orderEntity.getTotalPrice());
        // System.out.println("Returning orderResponseDto " +
        // orderResponseDto.getOrderId());
        orderResponseDto.setCreatedAt(orderEntity.getCreatedAt());
        orderResponseDto.setDeliveredAt(orderEntity.getDeliveredAt());
        orderResponseDto.setPaidAt(orderEntity.getPaidAt());
        return orderResponseDto;
    }

    private OrderItemResponseDto mapToOrderItemResponseDto(OrderItemsEntity orderItemsEntity) {
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setImage(orderItemsEntity.getImage());
        orderItemResponseDto.setOrderEntityId(orderItemsEntity.getOrderId());
        orderItemResponseDto.setOrderItemId(orderItemsEntity.getOrderEntity().getOrderId());
        orderItemResponseDto.setProductEntityId(orderItemsEntity.getProductEntity().getProductId());
        orderItemResponseDto.setQuantity(orderItemsEntity.getQuantity());
        orderItemResponseDto.setPrice(orderItemsEntity.getPrice());

        return orderItemResponseDto;

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

    public Optional<OrderEntity> findOrderEntityById(int orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<OrderResponseDto> getAllUserOrders(int userId) {
        List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);
        List<OrderResponseDto> orderResponseDtos = orderEntities.stream().map(o -> mapToResponseDto(o))
                .collect(Collectors.toList());
        return orderResponseDtos;
    }

}