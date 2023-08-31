package com.order.repository.order;

import com.order.dto.order.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderCustomRepository {

    Page<OrderDto> findAllByUser(Long userSeq, Pageable pageable);

    Optional<OrderDto> findByIdAndUserId(Long orderId, Long userId);

}
