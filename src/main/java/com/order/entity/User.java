package com.order.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Email
  private String email;

  private String passwd;

  private int loginCount;

  @OneToMany(mappedBy = "user")
  private List<Role> roles;

  @LastModifiedDate
  private LocalDateTime lastLoginAt;

  @CreatedDate
  private LocalDateTime createAt;

  public User(Long id) {
    checkNotNull(id, "id must be provided");
    this.id = id;
  }

  public User(Long id, String name, String email, String passwd, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
    checkNotNull(email, "email must be provided");
    checkArgument(isNotEmpty(name), "name must be provided");
    this.id = id;
    this.name = name;
    this.email = email;
    this.passwd = passwd;
    this.loginCount = loginCount;
    this.lastLoginAt = lastLoginAt;
    this.createAt = createAt;
  }

  public User(Long id, String name, String email, String passwd, int loginCount) {
    checkArgument(isNotEmpty(name), "name must be provided");
    checkArgument(
      name.length() >= 1 && name.length() <= 10,
      "name length must be between 1 and 10 characters"
    );
    checkNotNull(email, "email must be provided");
    checkNotNull(passwd, "password must be provided");

    this.id = id;
    this.name = name;
    this.email = email;
    this.passwd = passwd;
    this.loginCount = loginCount;
  }

  public void login(PasswordEncoder passwordEncoder, String credentials) {
    if (!passwordEncoder.matches(credentials, passwd)) {
      throw new IllegalArgumentException("Bad credential");
    }
  }

  public void afterLoginSuccess() {
    loginCount++;
    lastLoginAt = now();
  }

  public Optional<LocalDateTime> getLastLoginAt() {
    return ofNullable(lastLoginAt);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("seq", id)
      .append("name", name)
      .append("email", email)
      .append("password", "[PROTECTED]")
      .append("loginCount", loginCount)
      .append("lastLoginAt", lastLoginAt)
      .append("createAt", createAt)
      .toString();
  }

}