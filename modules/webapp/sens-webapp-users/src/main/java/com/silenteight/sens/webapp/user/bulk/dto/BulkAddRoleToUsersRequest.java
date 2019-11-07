package com.silenteight.sens.webapp.user.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.user.dto.AddRoleRequest;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class BulkAddRoleToUsersRequest {

  @NonNull
  List<Long> userIds;

  @NonNull
  Role role;

  public Collection<AddRoleRequest> asAddRoleRequests() {
    return this.getUserIds()
               .stream()
               .map(id -> asAddRoleRequest(id, getRole()))
               .collect(toList());
  }

  private static AddRoleRequest asAddRoleRequest(Long userId, Role role) {
    return new AddRoleRequest(userId, role);
  }
}
