package com.order.repository;

import com.order.entity.QRole;
import com.order.entity.QUser;
import com.order.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.order.entity.QRole.role;
import static com.order.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(queryFactory.select(user)
                .from(user)
                .join(user.roles).fetchJoin()
                .where(user.email.eq(email))
                .fetchFirst()
        );
    }

    @Override
    public void updateAfterLogin(Long userId) {
        queryFactory.update(user)
                .set(user.loginCount, user.loginCount.add(1))
                .set(user.createAt, LocalDateTime.now())
                .where(user.id.eq(userId))
                .execute();
    }
}
