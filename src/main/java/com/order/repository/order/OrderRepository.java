package com.order.repository.order;

import com.order.entity.Order;
import com.order.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository{

}
