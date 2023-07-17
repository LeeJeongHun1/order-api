package com.order.orders;

import com.order.dto.user.LoginResult;
import com.order.utils.ApiUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class ReviewRestController {
    // TODO review 메소드 구현이 필요합니다.

    @PostMapping(path = "/{id}/review")
    public ApiUtils.ApiResult<LoginResult> review(@PathVariable Long id) {
        System.out.println(id);
        return null;
    }
}