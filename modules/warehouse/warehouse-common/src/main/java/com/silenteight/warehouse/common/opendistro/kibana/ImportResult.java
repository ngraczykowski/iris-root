package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Nullable;

import static java.lang.Boolean.TRUE;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportResult {

  @Nullable
  @JsonProperty("successCount")
  Long successCount;

  @Nullable
  @JsonProperty("success")
  Boolean success;

  @Nullable
  @JsonProperty("successResults")
  JsonNode successResults;

  @Nullable
  JsonNode error;

  @Nullable
  JsonNode message;

  boolean isError() {
    return !TRUE.equals(success);
  }
}
