package com.order.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.entity.Orders;
import com.order.entity.OrderDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private Long id;
    private List<OrderDetailDto> details = new ArrayList<>();
    private Double totalAmount;
    private LocalDateTime createAt;

    public OrderDto(Orders orders) {
        copyProperties(orders, this);
        for (OrderDetail detail : orders.getDetails()) {
            this.details.add(new OrderDetailDto(detail));
        }
    }
}