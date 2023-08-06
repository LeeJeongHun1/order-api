package com.order.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.dto.product.ProductDto;
import com.order.dto.review.ReviewDto;
import com.order.entity.Order;
import com.order.entity.OrderDetail;
import com.order.entity.Product;
import com.order.entity.Review;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDto {

    private Long id;
    private Long orderId;
    private String productName;
    private ReviewDto review;
    private Double price;
    private Integer quantity;
    private OrderDetail.State state;
    private LocalDateTime createAt;

    @Builder(builderClassName = "of", builderMethodName = "of")
    public OrderDetailDto(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.orderId = orderDetail.getOrder().getId();
        this.productName = orderDetail.getProduct().getName();
        this.review = orderDetail.getReview() == null ? null : new ReviewDto(orderDetail.getReview());
        this.price = orderDetail.getPrice();
        this.quantity = orderDetail.getQuantity();
        this.state = orderDetail.getState();
        this.createAt = orderDetail.getCreateAt();
    }

}

