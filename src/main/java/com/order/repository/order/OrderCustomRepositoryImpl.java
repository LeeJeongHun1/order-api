package com.order.repository.order;

import com.order.dto.order.OrderDetailDto;
import com.order.dto.order.OrderDto;
import com.order.dto.product.ProductDto;
import com.order.dto.review.ReviewDto;
import com.order.entity.Orders;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.Entity;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.order.entity.QOrderDetail.orderDetail;
import static com.order.entity.QOrders.orders;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderDto> findAllByUser(Long userId, Pageable pageable) {
        JPAQuery<Long> query = queryFactory
                .select(orders.id)
                .from(orders)
                .innerJoin(orders.user)
                .leftJoin(orders.details, orderDetail)
                .leftJoin(orderDetail.review)
                .leftJoin(orderDetail.product)
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .where(orders.user.id.eq(userId))
                .orderBy(getOrderSpecifier(Orders.class, pageable.getSort()))
                .groupBy(orders.id);

        List<Long> ids = query.fetch();

        List<OrderDto> list = queryFactory
                .from(orders)
                .innerJoin(orders.user)
                .leftJoin(orders.details, orderDetail)
                .leftJoin(orderDetail.review)
                .leftJoin(orderDetail.product)
                .where(orders.id.in(ids))
                .orderBy(getOrderSpecifier(Orders.class, pageable.getSort()))
                .transform(groupBy(orders.id).list(buildExpressionForOrder()));

        return new PageImpl<>(list, pageable, query.fetchCount());
    }

    @Override
    public Optional<OrderDto> findByIdAndUserId(Long orderId, Long userId) {
        Map<Long, OrderDto> transform = queryFactory
                .from(orders)
                .innerJoin(orders.user)
                .leftJoin(orders.details, orderDetail)
                .leftJoin(orderDetail.review)
                .leftJoin(orderDetail.product)
                .where(orders.user.id.eq(userId)
                        .and(orders.id.eq(orderId)))
                .transform(groupBy(orders.id).as(buildExpressionForOrder()));
        return Optional.ofNullable(transform.get(orderId));
    }

    private QBean<OrderDto> buildExpressionForOrder() {
        return Projections.fields(OrderDto.class,
                orders.id,
                list(Projections.fields(OrderDetailDto.class,
                                orderDetail.id,
                                Expressions.as(buildExpressionForProduct(), "product"),
                                Expressions.as(buildExpressionForReview(), "review"),
                                orderDetail.productName,
                                orderDetail.price,
                                orderDetail.quantity,
                                orderDetail.state,
                                orderDetail.rejectMsg,
                                orderDetail.rejectedAt,
                                orderDetail.completedAt,
                                orderDetail.createAt
                        )
                ).as("details"),
                orders.totalAmount,
                orders.createAt
        );
    }

    @NotNull
    private Expression<ReviewDto> buildExpressionForReview() {
        return Projections.fields(ReviewDto.class,
                orderDetail.review.id,
                orderDetail.review.product.id.as("productId"),
                orderDetail.review.content,
                orderDetail.review.createAt
        ).skipNulls();
    }

    @NotNull
    private Expression<ProductDto> buildExpressionForProduct() {
        return Projections.fields(ProductDto.class,
                orderDetail.product.id,
                orderDetail.product.name,
                orderDetail.product.reviewCount,
                orderDetail.product.createAt
        ).skipNulls();
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Class<?> clazz, Sort sort) {
        String tableName = clazz.getAnnotation(Entity.class).name();

        return sort.stream().map(order -> {
            PathBuilder<Object> pathBuilder = new PathBuilder<>(clazz, tableName);
            PathBuilder<Object> field = pathBuilder.get(order.getProperty());

            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            return new OrderSpecifier(direction, pathBuilder.get(field));

        }).toArray(OrderSpecifier[]::new);
    }

}
