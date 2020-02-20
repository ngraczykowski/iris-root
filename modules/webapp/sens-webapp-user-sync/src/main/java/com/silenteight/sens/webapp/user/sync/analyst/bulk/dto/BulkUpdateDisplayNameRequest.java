package com.silenteight.sens.webapp.user.sync.analyst.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase.UpdateUserDisplayNameCommand;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class BulkUpdateDisplayNameRequest {

  @NonNull
  List<UpdatedDisplayName> updatedDisplayNames;

  public Collection<UpdateUserDisplayNameCommand> asUpdateUserDisplayNameCommands() {
    return getUpdatedDisplayNames()
        .stream()
        .map(BulkUpdateDisplayNameRequest::asUpdateUserDisplayNameCommand)
        .collect(toList());
  }

  private static UpdateUserDisplayNameCommand asUpdateUserDisplayNameCommand(
      UpdatedDisplayName updatedDisplayName) {

    return UpdateUserDisplayNameCommand
        .builder()
        .username(updatedDisplayName.getUsername())
        .displayName(updatedDisplayName.getDisplayName())
        .build();
  }

  @Value
  public static class UpdatedDisplayName {

    @NonNull
    String username;

    String displayName;
  }
}
