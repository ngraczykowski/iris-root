package com.silenteight.serp.governance.policy.importing;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class ImportPolicyCommand {

  @NonNull
  InputStream inputStream;

  @NonNull
  String importedBy;
}
