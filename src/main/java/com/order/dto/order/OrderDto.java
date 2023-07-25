package com.order.dto.order;

import com.order.dto.review.ReviewDto;
import com.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
public class OrderDto {

    private Long id;
    private Long productId;
    private ReviewDto review;
    private String requestMsg;
    private String rejectMsg;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;

    private LocalDateTime createAt;

    public OrderDto(Order order) {
        this.productId = order.getProduct().getId();
        if (order.getReview() != null)
            this.review = new ReviewDto(order.getReview());
        copyProperties(order, this);
    }
}