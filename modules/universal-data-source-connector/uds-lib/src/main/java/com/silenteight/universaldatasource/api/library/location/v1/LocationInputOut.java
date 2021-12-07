package com.silenteight.universaldatasource.api.library.location.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.location.v1.LocationInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class LocationInputOut {

  String match;

  @Builder.Default
  List<LocationFeatureInputOut> locationFeatureInputs = List.of();

  static LocationInputOut createFrom(LocationInput input) {
    return LocationInputOut.builder()
        .match(input.getMatch())
        .locationFeatureInputs(input.getLocationFeatureInputsList().stream()
            .map(LocationFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
