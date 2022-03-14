package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.infrastructure.FeatureConfiguration.LIST_OF_STRINGS;
import static java.util.List.of;

@RequiredArgsConstructor
public class NationalityFeature implements FabFeature {

  static final String FEATURE_NAME = "features/nationalityCountry";
  private static final String JSON_PATH = "$.HittedEntity.Nationality";

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    return CountryFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyCountries(getAlertedPart(buildFeatureCommand.getParsedMessageData()))
        .watchlistCountries(getWatchlistPart(buildFeatureCommand.getMatch().getPayload()))
        .build();
  }

  private static List<String> getAlertedPart(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getCountryOfIncorporation());
  }

  List<String> getWatchlistPart(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(JSON_PATH, LIST_OF_STRINGS);
  }
}
