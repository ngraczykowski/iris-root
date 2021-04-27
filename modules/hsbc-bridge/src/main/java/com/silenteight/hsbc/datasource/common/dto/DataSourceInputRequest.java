package com.silenteight.hsbc.datasource.common.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

import static java.util.function.Predicate.not;

@Value
@Builder
public class DataSourceInputRequest {

  @NotNull List<String> matches;
  @NotNull List<String> features;
}
