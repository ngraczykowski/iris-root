package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Metadata {

  @NonNull
  String systemId;
  @NonNull
  String recordId;
}
