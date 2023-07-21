package com.order.orders;

import com.order.security.CustomUser;
import com.order.service.OrderService;
import com.order.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderRestController {

    private final OrderService orderService;
  // TODO findAll, findById, accept, reject, shipping, complete 메소드 구현이 필요합니다.

    @GetMapping("/")
    public ApiResult<?> getOrders(
            @AuthenticationPrincipal CustomUser customUser,
            @PageableDefault Pageable pageable) {
        return orderService.getOrders(customUser.getUserId(), pageable);
    }

    @GetMapping("/{id}")
    public void getOrder(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long id) {

    }
}