package com.order.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LoginRequest {

  @NotBlank(message = "principal must be provided")
  private String principal;

  @NotBlank(message = "credentials must be provided")
  private String credentials;


  public LoginRequest(String principal, String credentials) {
    this.principal = principal;
    this.credentials = credentials;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("principal", principal)
      .append("credentials", credentials)
      .toString();
  }

}