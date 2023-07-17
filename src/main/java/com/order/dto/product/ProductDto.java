package com.order.dto.product;

import com.order.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
public class ProductDto {

  private Long seq;

  private String name;

  private String details;

  private int reviewCount;

  private LocalDateTime createAt;

  public ProductDto(Product source) {
    copyProperties(source, this);

    this.details = source.getDetails().orElse(null);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("seq", seq)
      .append("name", name)
      .append("details", details)
      .append("reviewCount", reviewCount)
      .append("createAt", createAt)
      .toString();
  }

}