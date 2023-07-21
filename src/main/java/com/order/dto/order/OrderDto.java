package com.order.dto.order;

import com.order.dto.review.ReviewDto;
import com.order.entity.Order;
import com.order.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
public class OrderDto {

    private Long seq;
    private Long productId;
    private ReviewDto review;
    private Order.State state;
    private String requestMsg;
    private String rejectMsg;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;

    private LocalDateTime createAt;

    public OrderDto(Order order) {
        this.productId = order.getProduct().getSeq();
        if (order.getReview() != null)
            this.review = new ReviewDto(order.getReview());
        copyProperties(order, this);
    }
}