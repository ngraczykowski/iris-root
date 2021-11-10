package com.silenteight.hsbc.datasource.extractors.name;

import com.silenteight.hsbc.datasource.dto.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationResponseDto;

import java.util.Optional;

public interface NameInformationServiceClient {

  Optional<GetNameInformationResponseDto> getNameInformation(GetNameInformationRequestDto request);
}
