package com.silenteight.sens.webapp.backend.presentation.dto.user.dto;

import lombok.*;

import com.silenteight.sens.webapp.users.bulk.dto.Analyst;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnalystDto {

  @NonNull
  private String userName;

  private String displayName;

  public static Analyst mapToAnalyst(AnalystDto dto) {
    return new Analyst(dto.getUserName(), dto.getDisplayName());
  }
}
