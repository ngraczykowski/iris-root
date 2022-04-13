package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.domain.feature.AdditionalInfoHelper.getValue;
import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class GenderFeature implements FabFeature {

  static final String FEATURE_NAME = "features/gender";

  private static final String JSON_PATH = "$.HittedEntity.AdditionalInfo";
  private static final String FIELD_NAME = "Gender";

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    return GenderFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyGenders(getAlertedPart(buildFeatureCommand.getParsedMessageData()))
        .watchlistGenders(
            merge(buildFeatureCommand.getMatch().getPayloads(), this::getWatchlistPart))
        .build();
  }

  private static List<String> getAlertedPart(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getGender());
  }

  private List<String> getWatchlistPart(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(JSON_PATH, LIST_OF_STRINGS)
        .stream()
        .map(additionalInfo -> getValue(additionalInfo, FIELD_NAME))
        .collect(toList());
  }

}
