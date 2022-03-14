package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.infrastructure.FeatureConfiguration.LIST_OF_STRINGS;
import static java.util.List.of;

@RequiredArgsConstructor
public class GenderFeature implements FabFeature {

  static final String FEATURE_NAME = "features/gender";

  //TODO change this to correct path
  private static final String JSON_PATH = "$.HittedEntity.Gender";

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    return GenderFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyGenders(getAlertedPart(buildFeatureCommand.getParsedMessageData()))
        .watchlistGenders(getWatchlistPart(buildFeatureCommand.getMatch().getPayload()))
        .build();
  }

  private static List<String> getAlertedPart(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getGender());
  }

  private List<String> getWatchlistPart(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(JSON_PATH, LIST_OF_STRINGS);
  }

}
