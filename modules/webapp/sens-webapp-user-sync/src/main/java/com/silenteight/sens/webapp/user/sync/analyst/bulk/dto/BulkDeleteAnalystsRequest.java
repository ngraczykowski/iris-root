package com.silenteight.sens.webapp.user.sync.analyst.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.lock.LockUserUseCase.LockUserCommand;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class BulkDeleteAnalystsRequest {

  @NonNull
  List<String> usernames;

  public Collection<LockUserCommand> asLockUserCommands() {
    return getUsernames()
        .stream()
        .map(BulkDeleteAnalystsRequest::asLockUserCommand)
        .collect(toList());
  }

  private static LockUserCommand asLockUserCommand(String username) {
    return LockUserCommand
        .builder()
        .username(username)
        .build();
  }
}
