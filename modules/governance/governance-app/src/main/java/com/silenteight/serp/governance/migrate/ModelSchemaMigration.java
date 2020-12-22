package com.silenteight.serp.governance.migrate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
class ModelSchemaMigration {

  @NonNull
  @JsonProperty
  private List<String> features;
}
