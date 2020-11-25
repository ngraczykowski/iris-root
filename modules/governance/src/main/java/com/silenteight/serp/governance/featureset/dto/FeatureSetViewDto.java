package com.silenteight.serp.governance.featureset.dto;

import lombok.Value;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Value
public class FeatureSetViewDto {

  @NotNull
  Long id;

  @NotNull
  List<String> features;
}
