package com.silenteight.sens.webapp.user.sync.analyst.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase.AddRolesToUserCommand;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

@Value
public class BulkAddAnalystRoleRequest {

  @NonNull
  List<String> usernames;

  public Collection<AddRolesToUserCommand> asAddRolesToUserCommands() {
    return getUsernames()
        .stream()
        .map(BulkAddAnalystRoleRequest::asAddRolesToUserCommand)
        .collect(toList());
  }

  private static AddRolesToUserCommand asAddRolesToUserCommand(String username) {
    return AddRolesToUserCommand
        .builder()
        .username(username)
        .roles(singleton(ANALYST))
        .build();
  }
}
