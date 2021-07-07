package com.silenteight.searpaymentsmockup;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;

@Data
class AlertDto implements Serializable {

  @NotNull
  private String systemId;

  private OffsetDateTime createdAt = OffsetDateTime.now();
}
