package com.backend.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "items_price")
    private long itemsPrice;

    @Column(name = "tax_price")
    private long taxPrice;

    @Column(name = "shipping_price")
    private long shippingPrice;

    @Column(name = "total_price")
    private long totalPrice;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "is_delivered")
    private boolean isDelivered;

    @Column(name = "paid_at")
    private Date paidAt;

    @Column(name = "delivered_at")
    private Date deliveredAt;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddressEntity shippingAddress;

    @OneToMany(mappedBy = "orderEntity")
    private List<OrderItemsEntity> orderItemsEntity;

}
