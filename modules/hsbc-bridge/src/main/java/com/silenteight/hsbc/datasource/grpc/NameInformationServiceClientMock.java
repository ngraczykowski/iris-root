package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.dto.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import java.util.List;
import java.util.Optional;

class NameInformationServiceClientMock implements NameInformationServiceClient {

  @Override
  public Optional<GetNameInformationResponseDto> getNameInformation(GetNameInformationRequestDto request) {
    return Optional.of(GetNameInformationResponseDto.builder()
        .foreignAliases(List.of(new ForeignAliasDto("", "")))
        .build());
  }
}
