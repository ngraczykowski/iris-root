package com.silenteight.sens.webapp.user.sync.analyst.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase.UnlockUserCommand;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class BulkRestoreAnalystsRequest {

  @NonNull
  List<String> usernames;

  public Collection<UnlockUserCommand> asUnlockUserCommands() {
    return getUsernames()
        .stream()
        .map(BulkRestoreAnalystsRequest::asUnlockUserCommand)
        .collect(toList());
  }

  private static UnlockUserCommand asUnlockUserCommand(String username) {
    return UnlockUserCommand
        .builder()
        .username(username)
        .build();
  }
}
