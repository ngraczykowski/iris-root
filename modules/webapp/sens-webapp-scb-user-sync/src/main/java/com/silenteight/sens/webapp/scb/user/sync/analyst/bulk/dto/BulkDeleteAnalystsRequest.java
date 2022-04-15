package com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.scb.user.sync.analyst.domain.GnsOrigin.GNS_ORIGIN;
import static java.util.stream.Collectors.toList;

@Value
public class BulkDeleteAnalystsRequest {

  @NonNull
  List<String> usernames;

  public Collection<RemoveUserCommand> asRemoveUserCommands() {
    return getUsernames()
        .stream()
        .map(username -> BulkDeleteAnalystsRequest.asRemoveUserCommand(username))
        .collect(toList());
  }

  private static RemoveUserCommand asRemoveUserCommand(String username) {
    return RemoveUserCommand
        .builder()
        .username(username)
        .expectedOrigin(GNS_ORIGIN)
        .build();
  }
}
