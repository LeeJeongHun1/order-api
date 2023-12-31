package com.order.orders;

import com.order.dto.PageDto;
import com.order.dto.order.OrderDto;
import com.order.dto.order.OrderRejectDto;
import com.order.dto.order.OrderRequestDto;
import com.order.entity.OrderDetail;
import com.order.security.CustomUser;
import com.order.service.OrderService;
import com.order.utils.ApiUtils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.order.utils.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    @Operation(summary = "주문 목록", description = "주문 목록")
    @GetMapping
    public ResponseEntity<ApiResult<List<OrderDto>>> getOrders(
            @AuthenticationPrincipal CustomUser customUser,
            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC, size = 5)
            @ParameterObject Pageable pageable) {
        PageDto<OrderDto> orders = orderService.getOrders(customUser.getUserId(), pageable);
        return ResponseEntity.ok()
                .headers(orders.getHeader())
                .body(success(orders.getList()));
    }

    @Operation(summary = "주문 정보 조회", description = "주문 정보 조회")
    @GetMapping("/{id}")
    public ApiResult<OrderDto> getOrder(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long id) {
        return orderService.getOrder(id, customUser.getUserId());
    }

    @PostMapping()
    @Operation(summary = "상품 주문", description = "상품 주문")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = OrderRequestDto.class),
                    examples = @ExampleObject(
                            "{\n" +
                                    "  \"totalAmount\": 0.0,\n" +
                                    "  \"orderProducts\": [{ \"productId\": 1, \"quantity\": 1, \"price\": 1  },\n" +
                                    "          { \"productId\": 2, \"quantity\": 1, \"price\": 1.0  }]\n" +
                                    "}"
                    )))
    public ApiResult<OrderDto> order(
            @AuthenticationPrincipal CustomUser customUser,
            @Valid @RequestBody OrderRequestDto orderRequestDto
    ) {
        return orderService.order(customUser.getUserId(), orderRequestDto);
    }

    @Secured(value = "ROLE_ADMIN")
    @Operation(summary = "상품 주문 거절", description = "상품 주문 거절")
    @PatchMapping("/{id}/order-details/{orderDetailId}/reject")
    public ApiResult<Boolean> reject(
            @PathVariable Long id,
            @PathVariable Long orderDetailId,
            @Valid @RequestBody OrderRejectDto orderRejectDto
    ) {
        return orderService.reject(id, orderDetailId, orderRejectDto);
    }

    @Secured(value = "ROLE_ADMIN")
    @Operation(summary = "상품 배송", description = "상품 배송")
    @PatchMapping("/{id}/order-details/{orderDetailId}/shipping")
    public ApiResult<Boolean> shipping(
            @PathVariable Long id,
            @PathVariable Long orderDetailId
    ) {
        return orderService.shipping(id, orderDetailId);
    }

    @Operation(summary = "상품 주문 확정", description = "상품 주문 확정")
    @PatchMapping("/{id}/order-details/{orderDetailId}/complete")
    public ApiResult<Boolean> complete(
            @PathVariable Long id,
            @PathVariable Long orderDetailId
    ) {
        return orderService.complete(id, orderDetailId);
    }

}