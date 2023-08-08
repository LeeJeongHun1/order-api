package com.order.service;

import com.order.dto.order.OrderDetailDto;
import com.order.dto.order.OrderDto;
import com.order.dto.order.OrderProductDto;
import com.order.dto.order.OrderRequestDto;
import com.order.entity.Order;
import com.order.entity.OrderDetail;
import com.order.entity.Product;
import com.order.entity.User;
import com.order.exception.BadRequestException;
import com.order.exception.NotFoundException;
import com.order.repository.ProductRepository;
import com.order.repository.order.OrderDetailRepository;
import com.order.repository.order.OrderRepository;
import com.order.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.order.utils.ApiUtils.success;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ApiResult<List<OrderDto>> getOrders(Long userId, Pageable pageable) {
        List<OrderDto> list = orderRepository.findAllByUser(userId, pageable);
        return success(list);
    }

    public ApiResult<OrderDto> getOrder(Long orderId, Long userId) {
        OrderDto orderDto = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NotFoundException("Could not found order for " + orderId));
        return success(orderDto);
    }

    public OrderDto order(Long userId, OrderRequestDto orderRequestDto) {
        orderValidate(orderRequestDto);
        Order order = Order.of()
                .user(new User(userId))
                .requestMsg(orderRequestDto.getRequestMsg())
                .totalAmount(orderRequestDto.getOrderPrice())
                .build();
        Order savedOrder = orderRepository.save(order);
        for (OrderProductDto orderProduct : orderRequestDto.getOrderProducts()) {
            OrderDetail orderDetail = buildOrderDetail(savedOrder, orderProduct);
            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            savedOrder.getDetails().add(savedOrderDetail);
        }
        return new OrderDto(savedOrder);
    }

    private OrderDetail buildOrderDetail(Order order, OrderProductDto orderProduct) {
        Product product = findProductById(orderProduct.getProductId());
        validateProductPrice(product, orderProduct);

        product.decreaseStock(orderProduct.getQuantity());

        return OrderDetail.of()
                .order(order)
                .product(product)
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getPrice() * orderProduct.getQuantity())
                .state(OrderDetail.State.REQUESTED)
                .build();
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Could not find product for " + productId));
    }

    // TODO 할인 관련 로직 추가되야 함
    private void validateProductPrice(Product product, OrderProductDto orderProduct) {
        if (!Objects.equals(product.getPrice(), orderProduct.getPrice())) {
            throw new BadRequestException("Order price is incorrect for product " + orderProduct.getProductId());
        }
    }

    private synchronized void orderValidate(OrderRequestDto orderRequestDto) {
        Double totalPrice = orderRequestDto.getOrderProducts().stream()
                .mapToDouble(orderProduct -> {
                    Product product = findProductById(orderProduct.getProductId());
                    validateProductPrice(product, orderProduct);
                    return orderProduct.getPrice() * orderProduct.getQuantity();
                })
                .sum();

        if (!totalPrice.equals(orderRequestDto.getOrderPrice())) {
            throw new BadRequestException("Order total price is incorrect");
        }
    }

    public void accept(Long orderId, Long userId) {

    }

}
