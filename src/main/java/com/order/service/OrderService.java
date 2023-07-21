package com.order.service;

import com.order.dto.order.OrderDto;
import com.order.entity.Order;
import com.order.exception.NotFoundException;
import com.order.repository.order.OrderRepository;
import com.order.utils.ApiUtils;
import com.order.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.order.utils.ApiUtils.success;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public ApiResult<List<OrderDto>> getOrders(Long userId, Pageable pageable) {
        List<OrderDto> list = orderRepository.findAllByUser(userId, pageable)
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return success(list);
    }

    public ApiResult<OrderDto> getOrder(Long orderId, Long userId) {
        OrderDto orderDto = orderRepository.findBySeqAndUserSeq(orderId, userId)
                .map(OrderDto::new)
                .orElseThrow(() -> new NotFoundException("Could not found order for " + orderId));
        return success(orderDto);
    }

}
