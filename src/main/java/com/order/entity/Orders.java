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
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderDetail> details = new ArrayList<>();
    private Double totalAmount;
    @CreatedDate
    private LocalDateTime createAt;


    @Builder(builderClassName = "of", builderMethodName = "of")
    public Orders(Long id, User user, Double totalAmount, LocalDateTime createAt) {
        checkNotNull(user, "user must be provided");
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
        this.createAt = createAt;
    }
}
