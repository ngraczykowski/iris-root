package com.silenteight.sens.webapp.scb.report;

import lombok.Data;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

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
    return "Active";
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
