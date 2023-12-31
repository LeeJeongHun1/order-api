package com.order.entity;

import com.order.exception.SoldOutException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity(name = "product")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Integer reviewCount;

    @CreatedDate
    private LocalDateTime createAt;

    public Product(Long id) {
        checkNotNull(id, "product id  must be provided");
        this.id = id;
    }

    public Product(Long id, String name, Double price, Integer stock, int reviewCount, LocalDateTime createAt) {
        checkArgument(isNotEmpty(name), "name must be provided");
        checkArgument(
                name.length() >= 1 && name.length() <= 100,
                "name length must be between 1 and 100 characters"
        );
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviewCount = reviewCount;
        this.createAt = createAt;
    }

    public Product(Long id, String name, int reviewCount, LocalDateTime createAt) {
        this(id, name, null, null, reviewCount, createAt);
    }

    public void setName(String name) {
        checkArgument(isNotEmpty(name), "name must be provided");
        checkArgument(
                name.length() >= 1 && name.length() <= 100,
                "name length must be between 1 and 100 characters"
        );

        this.name = name;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void decreaseStock(int quantity) throws SoldOutException {
        if (stock < quantity) {
            throw new SoldOutException();
        }
        stock -= quantity;
    }
}