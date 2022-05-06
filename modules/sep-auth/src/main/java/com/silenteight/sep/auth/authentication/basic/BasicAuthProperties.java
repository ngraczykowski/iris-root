package com.silenteight.sep.auth.authentication.basic;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ConfigurationProperties(prefix = "sep.auth.basic")
@ConstructorBinding
@Validated
public class BasicAuthProperties {

  @Valid
  Set<BasicUser> users;

  @Data
  @ConstructorBinding
  @Validated
  static class BasicUser {

    @NotNull
    String username;

    @NotNull
    String password;

    @NotNull
    @Size(min = 1)
    List<String> roles;

    UserDetails getUserDetails(Function<String, String> encoder) {
      return User.builder()
          .username(getUsername())
          .password(getPassword())
          .passwordEncoder(encoder)
          .authorities(getRoles().toArray(new String[0]))
          .build();
    }
  }
}

