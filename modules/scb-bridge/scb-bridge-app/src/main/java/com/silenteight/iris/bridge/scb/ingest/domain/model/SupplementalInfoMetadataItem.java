/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class SupplementalInfoMetadataItem {

  @NotNull
  @JsonProperty(value = "supplementalInfo", required = true)
  private SupplementalInfo supplementalInfo;

  @Data
  @Validated
  public class SupplementalInfo {

    @NotBlank
    @JsonProperty(value = "datasetId", required = true)
    private String datasetId;

    @NotBlank
    @JsonProperty(value = "datasetName", required = true)
    private String datasetName;

    @NotBlank
    @JsonProperty(value = "uniqueCustId", required = true)
    private String uniqueCustId;

    @NotBlank
    @JsonProperty(value = "masterId", required = true)
    private String masterId;

    @NotBlank
    @JsonProperty(value = "busDate", required = true)
    private String busDate;
  }
}
