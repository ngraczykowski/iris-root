package com.silenteight.sens.webapp.users.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.adapter.audit.AuditService;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.users.user.dto.UserView;

import java.time.Instant;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class UserReportService implements AuditService<UserView> {

  private static final String USER_ID_HEADER = "webapp_user_id";
  private static final String USER_NAME_HEADER = "user_name";
  private static final String USER_TYPE_HEADER = "type";
  private static final String SUPER_USER_HEADER = "super_user";
  private static final String ACTIVE_HEADER = "active";
  private static final String ROLES_HEADER = "roles";
  private static final String LAST_LOGIN_AT_HEADER = "last_login_at";
  private static final String CREATED_AT_HEADER = "created_at";
  private static final String DISPLAY_NAME_HEADER = "display_name";

  @NonNull
  private final UserFinder userFinder;

  @Override
  public CsvBuilder<UserView> generateAuditReport() {
    return new CsvBuilder<>(userFinder.findAll().stream())
        .cell(USER_ID_HEADER, v -> String.valueOf(v.getId()))
        .cell(USER_NAME_HEADER, UserView::getUserName)
        .cell(DISPLAY_NAME_HEADER, UserView::getDisplayName)
        .cell(USER_TYPE_HEADER, v -> v.getType().toString())
        .cell(SUPER_USER_HEADER, a -> a.isSuperUser() ? "1" : "0")
        .cell(ACTIVE_HEADER, a -> a.isActive() ? "1" : "0")
        .cell(ROLES_HEADER, UserView::getRoleNames)
        .cell(LAST_LOGIN_AT_HEADER, a -> instantToString(a.getLastLoginAt()))
        .cell(CREATED_AT_HEADER, a -> instantToString(a.getCreatedAt()));
  }

  private static String instantToString(Instant lastLoginAt) {
    return ofNullable(lastLoginAt).map(Instant::toString).orElse("");
  }
}
