package com.order.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role {

  public enum RoleType {

    USER,
    ADMIN

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(value = EnumType.STRING)
  private RoleType roleType;


}