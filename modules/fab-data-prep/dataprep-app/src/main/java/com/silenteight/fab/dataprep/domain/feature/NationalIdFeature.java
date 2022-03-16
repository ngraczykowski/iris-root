package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.nationalid.v1.NationalIdFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;
import static java.util.List.of;

@RequiredArgsConstructor
public class NationalIdFeature implements FabFeature {

  static final String FEATURE_NAME = "features/nationalIdDocument";
  private static final String COUNTRY_JSON_PATH = "$.HittedEntity.AdditionalInfo";
  //TODO is it correct?
  private static final String DOCUMENT_JSON_PATH = "$.HittedEntity.Codes[*]";

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    ParsedMessageData parsedMessageData = buildFeatureCommand.getParsedMessageData();
    JsonNode payload = buildFeatureCommand.getMatch().getPayload();
    return NationalIdFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyCountries(getAlertedPartCountry(parsedMessageData))
        .watchlistCountries(getWatchlistPartCountry(payload))
        .alertedPartyDocumentNumbers(getAlertedPartDocument(parsedMessageData))
        .watchlistDocumentNumbers(getWatchlistPartDocument(payload))
        .build();
  }

  private static List<String> getAlertedPartCountry(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getCountryOfIncorporation());
  }

  List<String> getWatchlistPartCountry(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(COUNTRY_JSON_PATH, LIST_OF_STRINGS);
  }

  private static List<String> getAlertedPartDocument(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getNationalId());
  }

  List<String> getWatchlistPartDocument(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(DOCUMENT_JSON_PATH, LIST_OF_STRINGS);
  }
}
