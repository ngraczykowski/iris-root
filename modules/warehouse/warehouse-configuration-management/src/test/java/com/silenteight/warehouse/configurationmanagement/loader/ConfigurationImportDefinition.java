package com.silenteight.warehouse.configurationmanagement.loader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationImportDefinition {

  @JsonProperty("tenant")
  private String tenant;

  @JsonProperty("saved_objects")
  private String savedObjects;

  @JsonProperty("report_instances")
  private String reportInstances;
}

