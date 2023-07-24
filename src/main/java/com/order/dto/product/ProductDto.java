package com.order.dto.product;

import com.order.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
public class ProductDto {

  private Long id;
  private String name;
  private Double price;
  private Integer stock;
  private int reviewCount;
  private LocalDateTime createAt;

  public ProductDto(Product source) {
    copyProperties(source, this);

  }

}