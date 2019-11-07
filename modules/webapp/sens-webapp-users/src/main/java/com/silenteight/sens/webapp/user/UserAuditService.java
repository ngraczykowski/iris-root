package com.silenteight.sens.webapp.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.adapter.audit.AuditService;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.user.dto.UserAuditDto;

@RequiredArgsConstructor
public class UserAuditService implements AuditService<UserAuditDto> {

  static final String USER_ID_HEADER = "webapp_user_id";
  static final String USER_NAME_HEADER = "user_name";
  static final String USER_TYPE_HEADER = "type";
  static final String SUPER_USER_HEADER = "super_user";
  static final String ACTIVE_HEADER = "active";
  static final String ROLES_HEADER = "roles";
  static final String LAST_LOGIN_AT_HEADER = "last_login_at";
  static final String CREATED_AT_HEADER = "created_at";
  static final String DISPLAY_NAME_HEADER = "display_name";
  @SuppressWarnings("squid:S2068")
  static final String PASSWORD_CHANGED_HEADER = "password_changed";
  static final String AUDITED_OPERATION_HEADER = "audited_operation";
  static final String AUDITED_AT_HEADER = "audited_at";
  static final String MODIFIED_BY_HEADER = "modified_by";

  @NonNull
  private final UserFinder userFinder;

  @Override
  public CsvBuilder<UserAuditDto> generateAuditReport() {
    return new CsvBuilder<>(userFinder.findAudited().stream())
        .cell(USER_ID_HEADER, a -> a.getUserId().toString())
        .cell(USER_NAME_HEADER, UserAuditDto::getUserName)
        .cell(DISPLAY_NAME_HEADER, UserAuditDto::getDisplayName)
        .cell(PASSWORD_CHANGED_HEADER, a -> a.isPasswordChanged() ? "1" : "0")
        .cell(SUPER_USER_HEADER, a -> a.isSuperUser() ? "1" : "0")
        .cell(ACTIVE_HEADER, a -> a.isActive() ? "1" : "0")
        .cell(ROLES_HEADER, UserAuditDto::getRolesString)
        .cell(AUDITED_OPERATION_HEADER, UserAuditDto::getAuditedOperation)
        .cell(AUDITED_AT_HEADER, a -> a.getAuditedAt().toString())
        .cell(MODIFIED_BY_HEADER, UserAuditDto::getModifiedBy);
  }
}
