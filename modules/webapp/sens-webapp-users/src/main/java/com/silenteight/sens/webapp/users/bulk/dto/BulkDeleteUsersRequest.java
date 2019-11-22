package com.silenteight.sens.webapp.users.bulk.dto;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class BulkDeleteUsersRequest {

  @NonNull
  List<String> userNames;
}
