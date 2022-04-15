package com.silenteight.sens.webapp.user.list;

import lombok.*;
import lombok.Builder.Default;

import java.time.OffsetDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {

  private String userName;
  private String displayName;
  @NonNull
  private String origin;
  @NonNull
  @Default
  private List<String> roles = emptyList();
  @NonNull
  @Default
  private List<String> countryGroups = emptyList();
  private OffsetDateTime lastLoginAt;
  private OffsetDateTime createdAt;
  private OffsetDateTime lockedAt;
}
