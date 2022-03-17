package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1.BankIdentificationCodesFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;

@RequiredArgsConstructor
public class BicFeature implements FabFeature {

  static final String FEATURE_NAME = "features/bic";
  private static final String MATCHING_TEXT_PATH = "$.MatchingText";
  private static final String TYPE_PATH = "$.HittedEntity.Type";
  private static final String BIC_PATH = "$.HittedEntity.Codes[*]";    //TODO is it correct?
  //TODO is it correct?
  private static final String SEARCH_CODES_PATH = "$.HittedEntity.Codes[*]";

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    JsonNode payload = buildFeatureCommand.getMatch().getPayload();
    return BankIdentificationCodesFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyMatchingField(getAlertedPart(buildFeatureCommand.getParsedMessageData()))
        .watchListMatchingText(getMatchingTexts(payload))
        .watchlistType(getWatchlistType(payload))
        .watchlistBicCodes(getWatchlistPart(payload))
        .watchlistSearchCodes(getWatchlistSearchCodes(payload))
        .build();
  }

  private static String getAlertedPart(ParsedMessageData parsedMessageData) {
    return parsedMessageData.getSwiftBic();
  }

  private List<String> getWatchlistPart(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(BIC_PATH, LIST_OF_STRINGS);
  }

  private List<String> getWatchlistSearchCodes(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(SEARCH_CODES_PATH, LIST_OF_STRINGS);
  }

  private String getWatchlistType(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(TYPE_PATH, LIST_OF_STRINGS)
        .stream()
        .findFirst()
        .orElse("");
  }

  private String getMatchingTexts(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(MATCHING_TEXT_PATH, LIST_OF_STRINGS)
        .stream()
        .findFirst()
        .orElse("");
  }
}
