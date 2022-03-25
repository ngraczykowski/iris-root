package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut;
import com.silenteight.universaldatasource.api.library.date.v1.EntityTypeOut;
import com.silenteight.universaldatasource.api.library.date.v1.SeverityModeOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;
import static java.util.List.of;

@RequiredArgsConstructor
public class DateFeature implements FabFeature {

  static final String FEATURE_NAME = "features/date";
  private static final String JSON_PATH = "$.HittedEntity.DatesOfBirth[*].DateOfBirth";

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    ParsedMessageData parsedMessageData = buildFeatureCommand.getParsedMessageData();
    return DateFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyDates(getAlertedPart(parsedMessageData))
        .watchlistDates(merge(buildFeatureCommand.getMatch().getPayloads(), this::getWatchlistPart))
        .alertedPartyType(getPartyType(parsedMessageData))
        .mode(SeverityModeOut.NORMAL)   //TODO is it correct?
        .build();
  }

  private static List<String> getAlertedPart(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getDob());
  }

  private List<String> getWatchlistPart(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(JSON_PATH, LIST_OF_STRINGS);
  }

  private static EntityTypeOut getPartyType(ParsedMessageData parsedMessageData) {
    switch (parsedMessageData.getCustomerTypeAsEnum()) {
      case INDIVIDUAL:
        return EntityTypeOut.INDIVIDUAL;
      case CORPORATE:
        return EntityTypeOut.ORGANIZATION;
      default:
        return EntityTypeOut.ENTITY_TYPE_UNSPECIFIED;
    }
  }
}
