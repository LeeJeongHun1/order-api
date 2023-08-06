package com.order.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDto {
    private Long productId;
    private Integer quantity;
    private Double price;
}
