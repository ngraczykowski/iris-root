package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import java.util.List;

@Builder
@Value
public class NameInputResponse {

  List<NameInputDto> inputs;
  NameInformationServiceClient client;
}
