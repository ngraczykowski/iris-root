package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;
import static java.util.List.of;

@RequiredArgsConstructor
public class PassportFeature implements FabFeature {

  static final String FEATURE_NAME = "features/passportNumberDocument";
  private static final String JSON_PATH = "$.HittedEntity.PassportNumber";    //TODO is it correct?

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    return DocumentFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyDocuments(getAlertedPart(buildFeatureCommand.getParsedMessageData()))
        .watchlistDocuments(
            merge(buildFeatureCommand.getMatch().getPayloads(), this::getWatchlistPart))
        .build();
  }

  private static List<String> getAlertedPart(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getPassportNum());
  }

  private List<String> getWatchlistPart(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(JSON_PATH, LIST_OF_STRINGS);
  }
}
