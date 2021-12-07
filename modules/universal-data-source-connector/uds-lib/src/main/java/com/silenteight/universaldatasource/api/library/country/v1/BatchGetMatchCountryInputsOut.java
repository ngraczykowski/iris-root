package com.silenteight.universaldatasource.api.library.country.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchCountryInputsOut {

  @Builder.Default
  List<CountryInputOut> countryMatches = List.of();

  static BatchGetMatchCountryInputsOut createFrom(BatchGetMatchCountryInputsResponse response) {
    return BatchGetMatchCountryInputsOut.builder()
        .countryMatches(response.getCountryMatchesList().stream()
            .map(CountryInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
