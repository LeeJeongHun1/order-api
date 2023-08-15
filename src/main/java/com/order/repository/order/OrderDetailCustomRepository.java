package com.order.repository.order;

import com.order.dto.order.OrderDetailDto;
import com.order.dto.order.OrderDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderDetailCustomRepository {

    Optional<OrderDetailDto> findOrderDetailById(Long id, Long userId);

    void updateReview(Long orderDetailId, Long reviewId);
}
