package com.order.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.dto.review.ReviewDto;
import com.order.entity.Order;
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
    private String requestMsg;
    private String rejectMsg;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;

    private LocalDateTime createAt;

    public OrderDto(Order order) {
        copyProperties(order, this);
        for (OrderDetail detail : order.getDetails()) {
            this.details.add(new OrderDetailDto(detail));
        }
    }
}