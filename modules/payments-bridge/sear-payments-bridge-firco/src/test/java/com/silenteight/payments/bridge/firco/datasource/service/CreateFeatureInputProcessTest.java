package com.silenteight.payments.bridge.firco.datasource.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.silenteight.payments.bridge.firco.datasource.service.DataSourceFixture.createAeAlert;
import static com.silenteight.payments.bridge.firco.datasource.service.DataSourceFixture.createHitAndWatchlistPartyData;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateFeatureInputProcessTest {

  private CreateFeatureInputProcess createFeatureInputProcess;
  private CreateFeatureInputsMock createFeatureInputsMock;

  @BeforeEach
  void setUp() {
    createFeatureInputsMock = new CreateFeatureInputsMock();
    createFeatureInputProcess = new CreateFeatureInputProcess(createFeatureInputsMock);
  }

  @Test
  void shouldCreateUnstructuredFeatureInput() {
    createFeatureInputProcess.createUnstructuredFeatureInputs(
        createAeAlert(), Map.of("matchId", createHitAndWatchlistPartyData()));
    var unstructuredFeatureInputs = createFeatureInputsMock.getFeatureInputUnstructureds();
    assertThat(unstructuredFeatureInputs.size()).isEqualTo(1);
    var input = unstructuredFeatureInputs.get(0);
    var nameMatchedText = input.getNameMatchedTextAgentData();
    assertThat(nameMatchedText.getMatchingTexts()).isEqualTo(
        List.of("matching", "matching2"));
    assertThat(nameMatchedText.getAlertedPartyName()).isEqualTo(List.of("matching"));
  }
}
