package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.extractors.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import static java.util.List.of;

class NameInformationServiceClientMock implements NameInformationServiceClient {

  @Override
  public GetNameInformationResponseDto getNameInformation(GetNameInformationRequestDto request) {
    return GetNameInformationResponseDto.builder()
        .firstName("")
        .lastName("")
        .aliases(of(""))
        .foreignAliases(of(new ForeignAliasDto("", "")))
        .build();
  }
}
