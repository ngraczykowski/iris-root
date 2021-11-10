package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetNameInformationRequestDto {

  @NonNull String watchlistUuid;
}
