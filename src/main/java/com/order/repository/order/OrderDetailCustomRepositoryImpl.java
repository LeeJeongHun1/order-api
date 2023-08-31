package com.order.repository.order;

import com.order.dto.order.OrderDetailDto;
import com.order.dto.product.ProductDto;
import com.order.dto.review.ReviewDto;
import com.order.entity.OrderDetail;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.order.entity.QOrderDetail.orderDetail;

@RequiredArgsConstructor
public class OrderDetailCustomRepositoryImpl implements OrderDetailCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<OrderDetailDto> findOrderDetailById(Long id, Long userId) {
        return Optional.ofNullable(
                queryFactory.select(buildExpressionForOrderDetail())
                        .from(orderDetail)
                        .innerJoin(orderDetail.orders.user)
                        .leftJoin(orderDetail.review)
                        .innerJoin(orderDetail.product)
                        .where(
                                orderDetail.id.eq(id),
                                orderDetail.orders.user.id.eq(userId)
                        )
                        .fetchFirst()
        );
    }


    @Override
    public void updateReview(Long orderDetailId, Long reviewId) {
        queryFactory.update(orderDetail)
                .set(orderDetail.review.id, reviewId)
                .where(orderDetail.id.eq(orderDetailId))
                .execute();
    }

    private QBean<OrderDetailDto> buildExpressionForOrderDetail() {
        return Projections.fields(OrderDetailDto.class,
                orderDetail.id,
                orderDetail.orders.id.as("orderId"),
                Expressions.as(buildExpressionForProduct(), "product"),
                Expressions.as(buildExpressionForReview(), "review"),
                orderDetail.price,
                orderDetail.quantity,
                orderDetail.state,
                orderDetail.createAt
//                order.id,
//                order.totalAmount,
//                order.requestMsg,
//                order.rejectMsg,
//                order.completedAt,
//                order.rejectedAt,
//                order.createAt
        );
    }

    @NotNull
    private Expression<ProductDto> buildExpressionForProduct() {
        return Projections.fields(ProductDto.class,
                orderDetail.product.id,
                orderDetail.product.name,
                orderDetail.product.createAt
        ).skipNulls();
    }

    @NotNull
    private Expression<ReviewDto> buildExpressionForReview() {
        return Projections.fields(ReviewDto.class,
                orderDetail.review.id,
                orderDetail.review.content,
                orderDetail.review.createAt
        ).skipNulls();
    }
}
