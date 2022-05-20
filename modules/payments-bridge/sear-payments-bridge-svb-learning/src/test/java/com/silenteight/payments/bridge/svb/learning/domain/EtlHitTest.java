package com.silenteight.payments.bridge.svb.learning.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EtlHitTest {

  @ParameterizedTest
  @CsvFileSource(
      resources = "/EtlHitTest.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parametrized(
      String fkcoVHitType,
      String firstWatchlistName,
      String fkcoVListCity,
      String fkcoVListState,
      String fkcoVListCountry,
      String expectedWlName
  ) {
    var hitComposite = HitComposite.builder()
        .fkcoVHitType(fkcoVHitType)
        .fkcoVListCity(fkcoVListCity)
        .fkcoVListState(fkcoVListState)
        .fkcoVListCountry(fkcoVListCountry)
        .fkcoVListName(firstWatchlistName)
        .build();

    var etlHit = EtlHit.builder()
        .hitComposite(hitComposite)
        .build();

    assertEquals(
        expectedWlName,
        etlHit.toNameAddressCrossmatchAgentRequest().getWatchlistName());
  }
}
