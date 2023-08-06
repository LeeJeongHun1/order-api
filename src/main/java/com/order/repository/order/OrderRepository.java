package com.order.repository.order;

import com.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository{
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

}
