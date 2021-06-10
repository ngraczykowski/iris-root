package com.silenteight.hsbc.datasource.common.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class DataSourceInputRequest {

  @NotNull List<String> matches;
  @NotNull List<String> features;
}
