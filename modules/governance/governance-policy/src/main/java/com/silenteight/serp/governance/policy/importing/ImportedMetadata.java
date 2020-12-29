package com.silenteight.serp.governance.policy.importing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "exportedAt" })
class ImportedMetadata {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
  private Instant exportedAt;
}
