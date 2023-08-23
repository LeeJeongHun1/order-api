package com.order.orders;

import com.order.security.CustomUser;
import com.order.dto.review.ReviewDto;
import com.order.dto.review.ReviewReqDto;
import com.order.service.ReviewService;
import com.order.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order-details")
public class ReviewRestController {

    private final ReviewService reviewService;

    @Operation(summary = "주문 상품 리뷰 작성", description = "주문 상품 리뷰 작성")
    @PostMapping(path = "/{detailId}/review")
    public ApiUtils.ApiResult<ReviewDto> review(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long detailId,
            @Valid @RequestBody ReviewReqDto reviewReqDto) {
        return reviewService.review(detailId, customUser.getUserId(), reviewReqDto);
    }
}