package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.extractors.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import java.util.List;
import java.util.Optional;

class NameInformationServiceClientMock implements NameInformationServiceClient {

  @Override
  public Optional<GetNameInformationResponseDto> getNameInformation(GetNameInformationRequestDto request) {
    return Optional.of(GetNameInformationResponseDto.builder()
        .firstName("")
        .lastName("")
        .aliases(List.of(""))
        .foreignAliases(List.of(new ForeignAliasDto("", "")))
        .build());
  }
}
