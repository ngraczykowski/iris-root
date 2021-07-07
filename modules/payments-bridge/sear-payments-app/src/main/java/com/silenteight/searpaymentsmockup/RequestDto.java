package com.silenteight.searpaymentsmockup;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
class RequestDto implements Serializable {

  @Null
  private String dc;

  @Valid
  @Size(min = 1)
  @NotNull
  private List<AlertDto> alerts;

  private OffsetDateTime createdAt = OffsetDateTime.now();
}
