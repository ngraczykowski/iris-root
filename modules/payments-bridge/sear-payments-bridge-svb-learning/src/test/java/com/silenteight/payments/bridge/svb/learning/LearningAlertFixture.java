package com.silenteight.payments.bridge.svb.learning;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.svb.etl.model.AlertedPartyData;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LearningAlertFixture {

  public static LearningAlert createLearningAlert() {
    return LearningAlert
        .builder()
        .alertId(1)
        .matches(IntStream
            .range(1, 5)
            .mapToObj(LearningAlertFixture::createMatch)
            .collect(Collectors.toList()))
        .build();
  }

  public static LearningMatch createMatch(int matchId) {
    return LearningMatch
        .builder()
        .matchId(String.valueOf(matchId))
        .watchlistNames(List.of("watchlist1", "watchlist2"))
        .watchlistLocation("watchlist1")
        .matchType("type")
        .matchingTexts(List.of("match1", "match2"))
        .matchedFieldValue("matchedField")
        .alertedPartyData(
            AlertedPartyData
                .builder()
                .addresses(List.of("adrress1", "address2"))
                .nameAddresses(List.of("nameaddress1", "nameaddress2"))
                .ctryTowns(List.of("ctrTown1", "ctrTown1"))
                .names(List.of("name1", "name2"))
                .build())
        .entityType(EntityType.ENTITY_TYPE_UNSPECIFIED)
        .build();
  }
}
