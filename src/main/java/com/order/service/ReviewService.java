package com.order.service;

import com.order.dto.review.ReviewDto;
import com.order.dto.review.ReviewReqDto;
import com.order.entity.Order;
import com.order.entity.Review;
import com.order.exception.BadRequestException;
import com.order.exception.NotFoundException;
import com.order.repository.order.OrderRepository;
import com.order.repository.ReviewRepository;
import com.order.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.order.utils.ApiUtils.success;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ApiResult<ReviewDto> review(Long orderId, Long userSeq, ReviewReqDto reviewReqDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Could not found order for " + orderId));
        if (order.getState() != Order.State.COMPLETED) {
            throw new BadRequestException(
                    String.format("Could not write review for order %d because state(REQUESTED) is not allowed", orderId));
        }
        // 중복 리뷰
        if (Objects.nonNull(order.getReview())) {
            throw new BadRequestException(
                    String.format("Could not write review for order %d because have already written", orderId));
        }
        Review review = Review.builder()
                .user(order.getUser())
                .product(order.getProduct())
                .content(reviewReqDto.getContent())
                .build();
        Review save = reviewRepository.save(review);
        return success(new ReviewDto(save));
    }

}
