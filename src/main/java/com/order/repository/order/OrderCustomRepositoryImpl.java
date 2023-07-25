package com.order.repository.order;

import com.order.entity.Order;
import com.order.entity.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

import java.util.List;

import static com.order.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findAllByUser(Long userSeq, Pageable pageable) {
        return queryFactory.select(order)
                .from(order)
                .innerJoin(order.user).fetchJoin()
                .leftJoin(order.review).fetchJoin()
                .innerJoin(order.product).fetchJoin()
                .where(order.user.id.eq(userSeq))
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .fetch();
    }
}
