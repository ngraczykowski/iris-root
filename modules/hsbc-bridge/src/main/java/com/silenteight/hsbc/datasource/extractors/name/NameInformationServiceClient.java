package com.silenteight.hsbc.datasource.extractors.name;

public interface NameInformationServiceClient {

  GetNameInformationResponseDto getNameInformation(GetNameInformationRequestDto request);
}
