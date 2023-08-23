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

    /**
     * 주문 목록
     *
     * @param userId
     * @param pageable
     * @return
     */
    public ApiResult<List<OrderDto>> getOrders(Long userId, Pageable pageable) {
        List<OrderDto> list = orderRepository.findAllByUser(userId, pageable);
        return success(list);
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
        return success(new OrderDto(savedOrder));
    }

    /**
     * 주문 상세 builder
     * 상품 가격 검증
     * 상품 재고 차감
     *
     * @param order
     * @param orderProduct
     * @return
     */
    private synchronized OrderDetail buildOrderDetail(Order order, OrderProductDto orderProduct) {
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

        if (!totalPrice.equals(orderRequestDto.getOrderPrice())) {
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
        OrderDetail orderDetail = orderDetailRepository.findByIdAndOrderId(orderDetailId, orderId)
                .orElseThrow(() -> new NotFoundException("Could not found order info for " + orderId));
        if (orderDetail.getState() == OrderDetail.State.REQUESTED) {
            orderDetail.complete();
        } else {
            return success(false);
        }
        return success(true);
    }

    /**
     * 주문 거절
     *
     * @param orderId
     * @param orderDetailId
     * @return
     */
    public ApiResult<Boolean> reject(Long orderId, Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findByIdAndOrderId(orderDetailId, orderId)
                .orElseThrow(() -> new NotFoundException("Could not found order info for " + orderId));
        if (orderDetail.getState() == OrderDetail.State.REQUESTED) {
            orderDetail.reject("reject message");
            return success(true);
        } else {
            return success(false);
        }
    }

    /**
     * 주문상태 변경
     * @param orderId
     * @param orderDetailId
     * @param state
     * @return
     */
    public ApiResult<Boolean> accept(Long orderId, Long orderDetailId, OrderDetail.State state) {
        OrderDetail orderDetail = orderDetailRepository.findByIdAndOrderId(orderDetailId, orderId)
                .orElseThrow(() -> new NotFoundException("Could not found order info for " + orderId));

        if (orderDetail.getState() == OrderDetail.State.REQUESTED) {
            if (state == OrderDetail.State.COMPLETED) {
                orderDetail.complete();
            } else if (state == OrderDetail.State.REJECT){
                orderDetail.reject("reject message");
            } else if (state == OrderDetail.State.SHIPPING) {
                orderDetail.shipping();
            }
            return success(true);
        } else {
            return success(false);
        }

    }

}
