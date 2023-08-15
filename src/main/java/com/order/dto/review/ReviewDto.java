package com.order.dto.review;

import com.order.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

  private Long id;

  private Long productId;

  private String content;

  private LocalDateTime createAt;

//  public ReviewDto(Product source) {
//    copyProperties(source, this);
//
//    this.details = source.getDetails().orElse(null);
//  }


  public ReviewDto(Review review) {
    this.productId = review.getProduct().getId();
    copyProperties(review, this);
  }
}