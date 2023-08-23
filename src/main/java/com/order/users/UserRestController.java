package com.order.users;

import com.order.dto.user.LoginRequest;
import com.order.dto.user.UserDto;
import com.order.security.CustomUser;
import com.order.service.UserService;
import com.order.utils.ApiUtils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.order.utils.ApiUtils.success;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ApiResult<?> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        return success(userService.login(loginRequest));
    }

    @Operation(summary = "내 정록", description = "내 정보")
    @GetMapping(path = "me")
    public ApiResult<UserDto> me(
            @AuthenticationPrincipal CustomUser customUser
    ) {
        return success(userService.getUser(customUser.getUserId()));
    }


//    @Operation(summary = "주문 목록", description = "주문 목록")
//    @GetMapping("/orders")
//    public ApiResult<List<OrderDto>> getOrders(
//            @AuthenticationPrincipal CustomUser customUser,
//            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC)
//            @ParameterObject Pageable pageable) {
//        return orderService.getOrders(customUser.getUserId(), pageable);
//    }
//
//    @Operation(summary = "주문 정보 조회", description = "주문 정보 조회")
//    @GetMapping("/orders/{id}")
//    public ApiResult<OrderDto> getOrder(
//            @AuthenticationPrincipal CustomUser customUser,
//            @PathVariable Long id) {
//        return orderService.getOrder(id, customUser.getUserId());
//    }
}
