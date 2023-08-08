package com.order.repository.order;

import com.order.dto.order.OrderDetailDto;
import com.order.dto.order.OrderDto;
import com.order.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.order.entity.QOrder.order;
import static com.order.entity.QOrderDetail.orderDetail;
import static com.order.entity.QProduct.product;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderDto> findAllByUser(Long userId, Pageable pageable) {
        return queryFactory
                .from(order)
                .innerJoin(order.user)
                .leftJoin(order.details, orderDetail)
                .leftJoin(product).on(orderDetail.product.eq(product))
                .where(order.user.id.eq(userId))
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .transform(groupBy(order.id).list(buildExpressionForOrder()));
    }

    @Override
    public Optional<OrderDto> findByIdAndUserId(Long orderId, Long userId) {
        Map<Long, OrderDto> transform = queryFactory
                .from(order)
                .innerJoin(order.user)
                .leftJoin(order.details, orderDetail)
                .leftJoin(product).on(orderDetail.product.eq(product))
                .where(order.user.id.eq(userId)
                        .and(order.id.eq(orderId)))
                .transform(groupBy(order.id).as(buildExpressionForOrder()));
        return Optional.ofNullable(transform.get(orderId));
    }

    private static QBean<OrderDto> buildExpressionForOrder() {
        return Projections.fields(OrderDto.class,
                order.id,
                list(Projections.fields(OrderDetailDto.class,
                                orderDetail.id,
                                product.name.as("productName"),
                                orderDetail.price,
                                orderDetail.quantity,
                                orderDetail.state
                        )
                ).as("details"),
                order.totalAmount,
                order.requestMsg,
                order.rejectMsg,
                order.completedAt,
                order.rejectedAt,
                order.createAt
        );
    }
}
