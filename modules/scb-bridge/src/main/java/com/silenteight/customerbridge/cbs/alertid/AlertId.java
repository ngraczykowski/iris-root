package com.silenteight.customerbridge.cbs.alertid;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Value
public class AlertId {

  @NonNull
  String systemId;

  @NotNull
  @Size(min = 1)
  String batchId;
}

