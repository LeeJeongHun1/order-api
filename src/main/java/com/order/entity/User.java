package com.order.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  private String name;

  @Email
  private String email;

  private String passwd;

  private int loginCount;

  @LastModifiedDate
  private LocalDateTime lastLoginAt;

  @CreatedDate
  private LocalDateTime createAt;

  public User(Long seq, String name, String email, String passwd, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
    checkNotNull(email, "email must be provided");
    checkArgument(isNotEmpty(name), "name must be provided");
    this.seq = seq;
    this.name = name;
    this.email = email;
    this.passwd = passwd;
    this.loginCount = loginCount;
    this.lastLoginAt = lastLoginAt;
    this.createAt = createAt;
  }

  public User(Long seq, String name, String email, String passwd, int loginCount) {
    checkArgument(isNotEmpty(name), "name must be provided");
    checkArgument(
      name.length() >= 1 && name.length() <= 10,
      "name length must be between 1 and 10 characters"
    );
    checkNotNull(email, "email must be provided");
    checkNotNull(passwd, "password must be provided");

    this.seq = seq;
    this.name = name;
    this.email = email;
    this.passwd = passwd;
    this.loginCount = loginCount;
//    this.lastLoginAt = lastLoginAt;
//    this.createAt = defaultIfNull(createAt, now());
  }


//  public String newJwt(Jwt jwt, String[] roles) {
//    Jwt.Claims claims = Jwt.Claims.of(seq, name, roles);
//    return jwt.create(claims);
//  }

  public void login(PasswordEncoder passwordEncoder, String credentials) {
    if (!passwordEncoder.matches(credentials, passwd)) {
      throw new IllegalArgumentException("Bad credential");
    }
  }

  public void afterLoginSuccess() {
    loginCount++;
    lastLoginAt = now();
  }

  public Long getSeq() {
    return seq;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswd() {
    return passwd;
  }

  public int getLoginCount() {
    return loginCount;
  }

  public Optional<LocalDateTime> getLastLoginAt() {
    return ofNullable(lastLoginAt);
  }

  public LocalDateTime getCreateAt() {
    return createAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(seq, user.seq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("seq", seq)
      .append("name", name)
      .append("email", email)
      .append("password", "[PROTECTED]")
      .append("loginCount", loginCount)
      .append("lastLoginAt", lastLoginAt)
      .append("createAt", createAt)
      .toString();
  }

}