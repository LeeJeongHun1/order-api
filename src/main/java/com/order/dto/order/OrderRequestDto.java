package com.order.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull
    private List<OrderProductDto> orderProducts;
    private String requestMsg;
    @NotNull
    private Double orderPrice;

}