package com.silenteight.sens.webapp.users.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.dto.CreateUserRequest;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Value
public class BulkCreateUsersRequest {

  @NonNull
  List<NewUser> externalUsers;

  public Collection<CreateUserRequest> asCreateUserRequests() {
    return getExternalUsers()
        .stream()
        .map(BulkCreateUsersRequest::asCreateUserRequest)
        .collect(toList());
  }

  private static CreateUserRequest asCreateUserRequest(NewUser newUser) {
    return CreateUserRequest
        .builder()
        .name(newUser.getName())
        .displayName(newUser.getDisplayName())
        .superUser(false)
        .roles(singletonList(newUser.getRole()))
        .build();
  }

  @Value
  public static class NewUser {

    @NonNull
    String name;

    @NonNull
    Role role;

    String displayName;
  }
}
