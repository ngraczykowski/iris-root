package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class NameInputResponse {

  @Builder.Default
  List<NameInputDto> inputs = emptyList();
  NameInformationServiceClient client;
}
