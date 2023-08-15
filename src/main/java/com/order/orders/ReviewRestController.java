package com.order.orders;

import com.order.security.CustomUser;
import com.order.dto.review.ReviewDto;
import com.order.dto.review.ReviewReqDto;
import com.order.service.ReviewService;
import com.order.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order-details")
public class ReviewRestController {

    private final ReviewService reviewService;

    @PostMapping(path = "/{detailId}/review")
    public ApiUtils.ApiResult<ReviewDto> review(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long detailId,
            @Valid @RequestBody ReviewReqDto reviewReqDto) {
        return reviewService.review(detailId, customUser.getUserId(), reviewReqDto);
    }
}