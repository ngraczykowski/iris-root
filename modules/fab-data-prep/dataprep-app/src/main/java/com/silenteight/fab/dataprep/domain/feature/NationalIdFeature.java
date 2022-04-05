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
public class NationalIdFeature implements FabFeature {

  static final String FEATURE_NAME = "features/nationalIdDocument";
  //TODO is it correct?
  private static final String DOCUMENT_JSON_PATH = "$.HittedEntity.Codes[*]";

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
    return of(
        parsedMessageData.getNationalId());
  }

  private List<String> getWatchlistPart(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(DOCUMENT_JSON_PATH, LIST_OF_STRINGS);
  }
}
