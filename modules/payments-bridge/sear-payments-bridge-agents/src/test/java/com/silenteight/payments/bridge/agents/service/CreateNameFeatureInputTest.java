package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateNameFeatureInputTest {

  private CreateNameFeatureInput createNameFeatureInput;

  @BeforeEach
  void beforeEach() {
    createNameFeatureInput = new CreateNameFeatureInput();
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/CreateDefaultNameFeatureInputUseCaseTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void createDefaultParametrizedTest(
      String alertedPartyName,
      String feature,
      String matchingTexts,
      String watchlistType,
      String watchlistNames,
      String expectedFeature,
      String expectedMatchingTexts,
      String expectedWatchlistNames,
      String expectedAlertedPartyName,
      String expectedAlertedPartyType
  ) {
    var nameAgentRequest = NameAgentRequest.builder()
        .alertedPartyNames(List.of(alertedPartyName))
        .feature(feature)
        .matchingTexts(List.of(matchingTexts))
        .watchlistType(WatchlistType.valueOf(watchlistType))
        .watchlistNames(List.of(watchlistNames))
        .build();

    var actual = createNameFeatureInput.createDefault(nameAgentRequest);

    var expected = NameFeatureInput
        .newBuilder()
        .setFeature(expectedFeature)
        .addAllMatchingTexts(List.of(expectedMatchingTexts))
        .addAllWatchlistNames(List.of(
            WatchlistName.newBuilder()
                .setName(expectedWatchlistNames)
                .setType(NameType.REGULAR)
                .build()))
        .addAllAlertedPartyNames(List.of(
            AlertedPartyName.newBuilder()
                .setName(expectedAlertedPartyName)
                .build()))
        .setAlertedPartyType(NameFeatureInput.EntityType.valueOf(expectedAlertedPartyType))
        .build();

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/CreateOrganizationNameFeatureInputUseCaseTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void createOrganizationNameParametrizedTest(
      String alertedPartyNames,
      String feature,
      String watchlistType,
      String watchlistPartyNames,
      String expectedFeature,
      String expectedWatchlistNames,
      String expectedAlertedPartyName
  ) {
    var nameAgentRequest = NameAgentRequest.builder()
        .feature(feature)
        .alertedPartyNames(List.of(alertedPartyNames))
        .watchlistType(WatchlistType.valueOf(watchlistType))
        .watchlistNames(List.of(watchlistPartyNames))
        .build();

    var actual = createNameFeatureInput
        .createForOrganizationNameAgent(nameAgentRequest);

    var expected = NameFeatureInput
        .newBuilder()
        .setFeature(expectedFeature)
        .addAllAlertedPartyNames(getExpectedAlertedPartyNames(expectedAlertedPartyName))
        .addAllWatchlistNames(List.of(
            WatchlistName.newBuilder()
                .setName(expectedWatchlistNames)
                .build()))
        .build();

    assertEquals(expected, actual);
  }

  private List<AlertedPartyName> getExpectedAlertedPartyNames(String expectedAlertedPartyName) {
    return StringUtils.isNotBlank(expectedAlertedPartyName) ?
           List.of(AlertedPartyName.newBuilder().setName(expectedAlertedPartyName).build()) :
           Collections.emptyList();
  }
}
