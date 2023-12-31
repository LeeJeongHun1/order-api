package com.order.repository.order;

import com.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, OrderDetailCustomRepository {
    Optional<OrderDetail> findByIdAndOrdersId(Long id, Long orderDetailId);

}
