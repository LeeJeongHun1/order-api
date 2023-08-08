package com.order.repository.order;

import com.order.dto.order.OrderDto;
import com.order.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderCustomRepository {

    List<OrderDto> findAllByUser(Long userSeq, Pageable pageable);

    Optional<OrderDto> findByIdAndUserId(Long orderId, Long userId);

}
