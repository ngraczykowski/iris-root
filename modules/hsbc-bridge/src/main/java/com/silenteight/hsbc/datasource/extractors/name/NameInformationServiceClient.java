package com.silenteight.hsbc.datasource.extractors.name;

import java.util.Optional;

public interface NameInformationServiceClient {

  Optional<GetNameInformationResponseDto> getNameInformation(GetNameInformationRequestDto request);
}
