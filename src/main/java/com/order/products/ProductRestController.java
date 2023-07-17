package com.order.products;

import com.order.dto.product.ProductDto;
import com.order.service.ProductService;
import com.order.utils.ApiUtils;
import com.order.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.order.utils.ApiUtils.success;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping(path = "{id}")
    public ApiResult<ProductDto> findById(@PathVariable Long id) {
        return success(productService.getProduct(id));
    }

    @GetMapping
    public ApiResult<List<ProductDto>> findAll() {
        return success(productService.getProducts());
    }

}