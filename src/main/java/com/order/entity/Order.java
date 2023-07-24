package com.order.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Order {

    public enum State {
        REQUESTED,
        ACCEPTED,
        SHIPPING,
        COMPLETED,
        REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
    private State state;
    private String requestMsg;
    private String rejectMsg;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    @CreatedDate
    private LocalDateTime createAt;


    public Order(Long id, User user, Product product, Review review, State state, String requestMsg, String rejectMsg, LocalDateTime completedAt, LocalDateTime rejectedAt, LocalDateTime createAt) {
        checkNotNull(user, "email must be provided");
        checkNotNull(product, "email must be provided");
        checkNotNull(review, "email must be provided");

        this.id = id;
        this.user = user;
        this.product = product;
        this.review = review;
        this.state = state;
        this.requestMsg = requestMsg;
        this.rejectMsg = rejectMsg;
        this.completedAt = completedAt;
        this.rejectedAt = rejectedAt;
        this.createAt = createAt;
    }
}

