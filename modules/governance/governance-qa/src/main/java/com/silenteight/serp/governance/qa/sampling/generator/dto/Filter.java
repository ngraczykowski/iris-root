package com.silenteight.serp.governance.qa.sampling.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class Filter {

  @NonNull
  String field;
  @NonNull
  List<String> values;
}