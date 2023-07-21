package com.order.repository.order;

import com.order.entity.Order;
import com.order.entity.Product;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository{
    Optional<Order> findBySeqAndUserSeq(Long orderSeq, Long userId);

}
