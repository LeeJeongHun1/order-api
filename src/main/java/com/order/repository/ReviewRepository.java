package com.order.repository;

import com.order.entity.Product;
import com.order.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserId(Long userId);

}
