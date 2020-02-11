package com.silenteight.sens.webapp.backend.reportscb;

import lombok.Data;

import com.silenteight.sens.webapp.common.time.DateFormatter;
import com.silenteight.sens.webapp.user.dto.UserDto;

import static java.util.Optional.ofNullable;

@Data
class ReportUserDto {

  private final UserDto user;
  private final String role;
  private final DateFormatter dateFormatter;

  String getUserName() {
    return user.getUserName();
  }

  String getCreatedAt() {
    return dateFormatter.format(user.getCreatedAt());
  }

  String getLastLoginAt() {
    return ofNullable(user.getLastLoginAt()).map(dateFormatter::format).orElse("");
  }

  String getAccountType() {
    return isAdmin() ? "Service" : "User";
  }

  String getAccountStatus() {
    return user.isActive() ? "Active" : "Disabled";
  }

  String isPrivileged() {
    return isAdmin() ? "Y" : "N";
  }

  String getAccountDescription() {
    return isAdmin() ? "User with all permissions" : "";
  }

  private boolean isAdmin() {
    return "admin".equalsIgnoreCase(role);
  }
}
