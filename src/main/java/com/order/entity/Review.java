package com.order.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;

@Entity(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private String content;
  @CreatedDate
  private LocalDateTime createAt;

  @Builder
  public Review(Long id, User user, Product product, String content, LocalDateTime createAt) {
    checkNotNull(content, "email must be provided");
    checkNotNull(user, "email must be provided");
    checkNotNull(product, "email must be provided");
    checkArgument(
            content.length() >= 1 && content.length() <= 1000,
            "name length must be between 1 and 10 characters"
    );
    this.id = id;
    this.user = user;
    this.product = product;
    this.content = content;
    this.createAt = createAt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Review user = (Review) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}