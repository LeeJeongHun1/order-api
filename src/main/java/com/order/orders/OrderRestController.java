package com.order.orders;

import com.order.dto.order.OrderDto;
import com.order.dto.order.OrderRequestDto;
import com.order.dto.product.ProductDto;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.order.utils.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping()
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

    @PostMapping()
    @Operation(summary = "상품 주문", description = "상품 주문")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = OrderRequestDto.class),
                    examples = @ExampleObject(
                            "{\n" +
                                    "  \"requestMsg\": \"request message\",\n" +
                                    "  \"orderPrice\": 0.0,\n" +
                                    "  \"orderProducts\": [{ \"productId\": 1, \"quantity\": 1, \"price\": 1  },\n" +
                                    "          { \"productId\": 2, \"quantity\": 1, \"price\": 1.0  }]\n" +
                                    "}"
                    )))
    public ApiResult<?> order(
            @AuthenticationPrincipal CustomUser customUser,
            @Valid @RequestBody OrderRequestDto orderRequestDto
    ) {
        return success(orderService.order(customUser.getUserId(), orderRequestDto));
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