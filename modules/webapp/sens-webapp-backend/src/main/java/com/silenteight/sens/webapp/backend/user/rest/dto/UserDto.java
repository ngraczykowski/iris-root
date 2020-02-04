package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.user.domain.UserType;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String userName;
  private String displayName;
  private UserType type;
  private List<String> roles;
  private OffsetDateTime lastLoginAt;
  private OffsetDateTime createdAt;
}
