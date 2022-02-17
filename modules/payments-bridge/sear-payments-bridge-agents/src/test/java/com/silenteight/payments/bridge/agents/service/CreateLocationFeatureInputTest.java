package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.agents.model.GeoAgentRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateLocationFeatureInputTest {

  private CreateLocationFeatureInput createLocationFeatureInput;

  @BeforeEach
  void beforeEach() {
    createLocationFeatureInput = new CreateLocationFeatureInput();
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/CreateLocationFeatureInputUseCaseTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parametrizedTest(
      String feature,
      String countryTown,
      String watchlistLocation,
      String expectedFeature,
      String expectedAlertedPartyLocation,
      String expectedWatchlistLocation
  ) {

    var actualLocationFeatureInput = createLocationFeatureInput.create(
        GeoAgentRequest.builder()
            .feature(feature)
            .alertedPartyLocation(countryTown)
            .watchlistLocation(watchlistLocation)
            .build()
    );

    var expectedLocationFeatureInput = LocationFeatureInput.newBuilder()
        .setFeature(expectedFeature)
        .setAlertedPartyLocation(expectedAlertedPartyLocation)
        .setWatchlistLocation(expectedWatchlistLocation)
        .build();

    assertEquals(expectedLocationFeatureInput, actualLocationFeatureInput);
  }
}
