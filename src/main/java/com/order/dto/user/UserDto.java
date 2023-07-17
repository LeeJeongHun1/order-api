package com.order.dto.user;

import com.order.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
public class UserDto {

  private String name;

  private String email;

  private int loginCount;

  private LocalDateTime lastLoginAt;

  private LocalDateTime createAt;

  public UserDto(User source) {
    copyProperties(source, this);
    this.lastLoginAt = source.getLastLoginAt().orElse(null);
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("name", name)
      .append("email", email)
      .append("loginCount", loginCount)
      .append("lastLoginAt", lastLoginAt)
      .append("createAt", createAt)
      .toString();
  }

}