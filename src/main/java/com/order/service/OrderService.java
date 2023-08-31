package com.order.service;

import com.order.dto.PageDto;
import com.order.dto.order.OrderDto;
import com.order.dto.order.OrderProductDto;
import com.order.dto.order.OrderRejectDto;
import com.order.dto.order.OrderRequestDto;
import com.order.entity.Orders;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.order.utils.ApiUtils.success;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    /**
     * 주문 목록
     *
     * @param userId
     * @param pageable
     * @return
     */
    public PageDto<OrderDto> getOrders(Long userId, Pageable pageable) {
        Page<OrderDto> allByUser = orderRepository.findAllByUser(userId, pageable);

        return new PageDto<>(allByUser);
    }

    /**
     * 주문 정보
     *
     * @param orderId
     * @param userId
     * @return
     */
    public ApiResult<OrderDto> getOrder(Long orderId, Long userId) {
        OrderDto orderDto = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NotFoundException("Could not found order for " + orderId));
        return success(orderDto);
    }

    /**
     * 상품 주문
     *
     * @param userId
     * @param orderRequestDto
     * @return
     */
    public ApiResult<OrderDto> order(Long userId, OrderRequestDto orderRequestDto) {
        orderValidate(orderRequestDto);
        Orders orders = Orders.of()
                .user(new User(userId))
                .totalAmount(orderRequestDto.getTotalAmount())
                .build();
        Orders savedOrders = orderRepository.save(orders);
        for (OrderProductDto orderProduct : orderRequestDto.getOrderProducts()) {
            OrderDetail orderDetail = buildOrderDetail(savedOrders, orderProduct);
            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            savedOrders.getDetails().add(savedOrderDetail);
        }
        return success(new OrderDto(savedOrders));
    }

    /**
     * 주문 상세 builder
     * 상품 가격 검증
     * 상품 재고 차감
     *
     * @param orders
     * @param orderProduct
     * @return
     */
    private synchronized OrderDetail buildOrderDetail(Orders orders, OrderProductDto orderProduct) {
        Product product = findProductById(orderProduct.getProductId());
        validateProductPrice(product, orderProduct);

        product.decreaseStock(orderProduct.getQuantity());

        return OrderDetail.of()
                .orders(orders)
                .product(product)
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getPrice() * orderProduct.getQuantity())
                .state(OrderDetail.State.REQUESTED)
                .build();
    }

    /**
     * 상품 조회
     *
     * @param productId
     * @return
     */
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

    /**
     * 주문 정보 검증
     *
     * @param orderRequestDto
     */
    private void orderValidate(OrderRequestDto orderRequestDto) {
        Double totalPrice = orderRequestDto.getOrderProducts().stream()
                .mapToDouble(orderProduct -> {
                    Product product = findProductById(orderProduct.getProductId());
                    validateProductPrice(product, orderProduct);
                    return orderProduct.getPrice() * orderProduct.getQuantity();
                })
                .sum();

        if (!totalPrice.equals(orderRequestDto.getTotalAmount())) {
            throw new BadRequestException("Order total price is incorrect");
        }
    }

    /**
     * 관리자가 주문 승인
     *
     * @param orderId
     * @param orderDetailId
     * @return
     */
    public ApiResult<Boolean> complete(Long orderId, Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findByIdAndOrdersId(orderDetailId, orderId)
                .orElseThrow(() -> new NotFoundException("Could not found order info for " + orderId));
        if (orderDetail.getState() == OrderDetail.State.SHIPPING) {
            orderDetail.complete();
            return success(true);
        }
        return success(false);
    }

    /**
     * 주문 거절
     *
     * @param orderId
     * @param orderDetailId
     * @return
     */
    public ApiResult<Boolean> reject(Long orderId, Long orderDetailId, OrderRejectDto orderRejectDto) {
        OrderDetail orderDetail = orderDetailRepository.findByIdAndOrdersId(orderDetailId, orderId)
                .orElseThrow(() -> new NotFoundException("Could not found order info for " + orderId));
        if (orderDetail.getState() == OrderDetail.State.REQUESTED) {
            orderDetail.reject(orderRejectDto.getRejectMsg());
            return success(true);
        }

        return success(false);
    }

    /**
     * 주문상태 변경
     *
     * @param orderId
     * @param orderDetailId
     * @return
     */
    public ApiResult<Boolean> shipping(Long orderId, Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findByIdAndOrdersId(orderDetailId, orderId)
                .orElseThrow(() -> new NotFoundException("Could not found order info for " + orderId));

        if (orderDetail.getState() == OrderDetail.State.REQUESTED) {
            orderDetail.shipping();
            return success(true);
        }
        return success(false);
    }

}
