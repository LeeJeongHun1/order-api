package com.order.service;

import com.order.dto.order.OrderDetailDto;
import com.order.dto.review.ReviewDto;
import com.order.dto.review.ReviewReqDto;
import com.order.entity.*;
import com.order.exception.BadRequestException;
import com.order.exception.NotFoundException;
import com.order.repository.ProductRepository;
import com.order.repository.order.OrderDetailRepository;
import com.order.repository.order.OrderRepository;
import com.order.repository.ReviewRepository;
import com.order.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.order.utils.ApiUtils.success;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ApiResult<ReviewDto> review(Long orderDetailId, Long userId, ReviewReqDto reviewReqDto) {
        OrderDetailDto orderDetailDto = orderDetailRepository.findOrderDetailById(orderDetailId, userId)
                .orElseThrow(() -> new NotFoundException("Could not found order for " + orderDetailId));

        // 승인되지 않은 주문
        if (orderDetailDto.getState() != OrderDetail.State.COMPLETED) {
            throw new BadRequestException(
                    String.format("Could not write review for orderDetail %d because state(REQUESTED) is not allowed", orderDetailId));
        }

        // 중복 리뷰
        if (Objects.nonNull(orderDetailDto.getReview())) {
            throw new BadRequestException(
                    String.format("Could not write review for orderDetail %d because have already written", orderDetailId));
        }

        Review review = Review.builder()
                .user(new User(userId))
                .product(new Product(orderDetailDto.getProduct().getId()))
                .content(reviewReqDto.getContent())
                .build();
        Review savedReview = reviewRepository.save(review);
        orderDetailRepository.updateReview(orderDetailId, savedReview.getId());
        increaseReviewCount(orderDetailDto.getProduct().getId());
        return success(new ReviewDto(savedReview));
    }


    private void increaseReviewCount(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Could not found product for " + productId));
        product.increaseReviewCount();
    }

}
