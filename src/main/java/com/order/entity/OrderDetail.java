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
        SHIPPING,
        REJECT,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @OneToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private String productName;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;
    private String rejectMsg;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder(builderClassName = "of", builderMethodName = "of")
    public OrderDetail(Long id, Orders orders, Product product, Review review, Double price, Integer quantity, State state) {
        this.id = id;
        this.orders = orders;
        this.product = product;
        this.productName = product.getName();
        this.review = review;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
    }

    public void complete() {
        this.state = State.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void reject(String rejectMsg) {
        this.state = State.REJECT;
        this.rejectMsg = rejectMsg;
        this.rejectedAt = LocalDateTime.now();
    }

    public void shipping() {
        this.state = State.SHIPPING;
    }
}

