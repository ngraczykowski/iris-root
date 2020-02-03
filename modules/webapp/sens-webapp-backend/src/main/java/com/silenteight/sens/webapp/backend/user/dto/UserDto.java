package com.silenteight.sens.webapp.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String userName;
  private String displayName;
  private List<String> roles;
  private OffsetDateTime lastLoginAt;
  private OffsetDateTime createdAt;
  private boolean isActive;
}
