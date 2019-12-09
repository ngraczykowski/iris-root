package com.silenteight.sens.webapp.users.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.users.user.dto.UpdateUserRequest;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class BulkUpdateUserDisplayNameRequest {

  @NonNull
  List<UpdatedUser> updatedUsers;

  public Collection<UpdateUserRequest> asUpdateUserRequests() {
    return getUpdatedUsers()
        .stream()
        .map(BulkUpdateUserDisplayNameRequest::asUpdateUserRequest)
        .collect(toList());
  }

  private static UpdateUserRequest asUpdateUserRequest(UpdatedUser updatedUser) {
    return UpdateUserRequest
        .builder()
        .userId(updatedUser.getUserId())
        .displayName(updatedUser.getDisplayName())
        .build();
  }
}
