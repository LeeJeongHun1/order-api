package com.order.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.dto.product.ProductDto;
import com.order.dto.review.ReviewDto;
import com.order.entity.OrderDetail;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OrderDetailDto {

    private Long id;
    private Long orderId;
    private ProductDto product;
    private ReviewDto review;

    private String productName;
    private Double price;
    private Integer quantity;
    private OrderDetail.State state;

    private String rejectMsg;

    private LocalDateTime rejectedAt;

    private LocalDateTime completedAt;
    private LocalDateTime createAt;

    @Builder(builderClassName = "of", builderMethodName = "of")
    public OrderDetailDto(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.orderId = orderDetail.getOrders().getId();
        this.product = new ProductDto(orderDetail.getProduct());
        this.productName = orderDetail.getProductName();
        this.review = orderDetail.getReview() == null ? null : new ReviewDto(orderDetail.getReview());
        this.price = orderDetail.getPrice();
        this.quantity = orderDetail.getQuantity();
        this.state = orderDetail.getState();
        this.createAt = orderDetail.getCreateAt();
    }

}

