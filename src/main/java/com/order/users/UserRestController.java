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

    @Operation(summary = "내 정보", description = "내 정보")
    @GetMapping(path = "me")
    public ApiResult<UserDto> me(
            @AuthenticationPrincipal CustomUser customUser
    ) {
        return success(userService.getUser(customUser.getUserId()));
    }

}
