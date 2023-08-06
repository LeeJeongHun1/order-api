package com.order.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "order_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class OrderDetail {

    public enum State {
        REQUESTED,
        ACCEPTED,
        SHIPPING,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;
    @CreatedDate
    private LocalDateTime createAt;

    @Builder(builderClassName = "of", builderMethodName = "of")
    public OrderDetail(Long id, Order order, Product product, Review review, Double price, Integer quantity, State state) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.review = review;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
    }
}

