package com.order.dto.user;

import com.order.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
public class LoginResult {

  private String token;

  private UserDto user;

  public LoginResult(String token, User user) {
    this.token = token;
    this.user = new UserDto(user);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("token", token)
      .append("user", user)
      .toString();
  }

}