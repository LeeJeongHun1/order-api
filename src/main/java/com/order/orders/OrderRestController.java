package com.order.orders;

import com.order.dto.order.OrderDto;
import com.order.security.CustomUser;
import com.order.service.OrderService;
import com.order.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/")
    public ApiResult<?> getOrders(
            @AuthenticationPrincipal CustomUser customUser,
            @PageableDefault Pageable pageable) {
        return orderService.getOrders(customUser.getUserId(), pageable);
    }

    @GetMapping("/{id}")
    public ApiResult<OrderDto> getOrder(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long id) {
        return orderService.getOrder(id, customUser.getUserId());
    }

    @PatchMapping("/{id}/accept")
    public void accept(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long id) {
    }

    @PatchMapping("/{id}/reject")
    public void reject(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long id) {
    }

    @PatchMapping("/{id}/shipping")
    public void shipping(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long id) {
    }

    @PatchMapping("/{id}/complete")
    public void complete(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long id) {
    }
}