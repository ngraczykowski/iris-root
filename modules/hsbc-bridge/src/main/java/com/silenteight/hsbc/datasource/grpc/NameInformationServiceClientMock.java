package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.extractors.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import java.util.Optional;

import static java.util.List.of;

class NameInformationServiceClientMock implements NameInformationServiceClient {

  @Override
  public Optional<GetNameInformationResponseDto> getNameInformation(GetNameInformationRequestDto request) {
    return Optional.of(GetNameInformationResponseDto.builder()
        .firstName("")
        .lastName("")
        .aliases(of(""))
        .foreignAliases(of(new ForeignAliasDto("", "")))
        .build());
  }
}
