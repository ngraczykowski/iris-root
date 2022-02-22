package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
class RbsImportCommand {

  @NonNull
  InputStream inputStream;
  @NonNull
  String fileName;
}
