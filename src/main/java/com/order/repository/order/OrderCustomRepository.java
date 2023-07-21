package com.order.repository.order;

import com.order.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderCustomRepository {

    List<Order> findAllByUser(Long userSeq, Pageable pageable);
}
