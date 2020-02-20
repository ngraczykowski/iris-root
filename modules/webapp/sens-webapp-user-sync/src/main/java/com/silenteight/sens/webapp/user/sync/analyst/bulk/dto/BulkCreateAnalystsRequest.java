package com.silenteight.sens.webapp.user.sync.analyst.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase.RegisterExternalUserCommand;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static com.silenteight.sens.webapp.user.registration.domain.RegistrationSource.GNS;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

@Value
public class BulkCreateAnalystsRequest {

  @NonNull
  List<NewAnalyst> newAnalysts;

  public Collection<RegisterExternalUserCommand> asRegisterExternalUserCommands() {
    return getNewAnalysts()
        .stream()
        .map(BulkCreateAnalystsRequest::asRegisterExternalUserCommand)
        .collect(toList());
  }

  private static RegisterExternalUserCommand asRegisterExternalUserCommand(NewAnalyst newAnalyst) {
    return RegisterExternalUserCommand
        .builder()
        .username(newAnalyst.getUsername())
        .displayName(newAnalyst.getDisplayName())
        .roles(singleton(ANALYST))
        .source(GNS)
        .build();
  }

  @Value
  public static class NewAnalyst {

    @NonNull
    String username;

    String displayName;
  }
}
