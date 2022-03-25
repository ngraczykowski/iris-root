package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.name.v1.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class NameFeature implements FabFeature {

  static final String FEATURE_NAME = "features/name";
  private static final String WATCH_LIST_NAME_PATH = "$.HittedEntity.Names[*].Name";
  private static final String MATCHING_TEXT_PATH = "$.MatchingText";

  private final ParseContext parseContext;

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    ParsedMessageData parsedMessageData = buildFeatureCommand.getParsedMessageData();
    Match match = buildFeatureCommand.getMatch();
    return NameFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .alertedPartyNames(getAlertedPart(parsedMessageData))
        .alertedPartyType(getPartyType(parsedMessageData))
        .watchlistNames(merge(match.getPayloads(), this::getWatchlistPart))
        .matchingTexts(merge(match.getPayloads(), this::getMatchingTexts))
        .build();
  }

  private static List<AlertedPartyNameOut> getAlertedPart(ParsedMessageData parsedMessageData) {
    return Stream.of(parsedMessageData.getName(), parsedMessageData.getShortName())
        .map(NameFeature::getAlertedPartyNameOut)
        .collect(toList());
  }

  private static AlertedPartyNameOut getAlertedPartyNameOut(String name) {
    return AlertedPartyNameOut.builder()
        .name(name)
        .build();
  }

  protected List<WatchlistNameOut> getWatchlistPart(JsonNode jsonNode) {
    return parseContext
        .parse(jsonNode)
        .read(WATCH_LIST_NAME_PATH, LIST_OF_STRINGS)
        .stream()
        .map(name -> getWatchlistNameOut(name, NameTypeOut.REGULAR))
        .collect(toList());

    //TODO add alias names
  }

  private static WatchlistNameOut getWatchlistNameOut(String name, NameTypeOut type) {
    return WatchlistNameOut.builder()
        .name(name)
        .type(type)
        .build();
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

  private List<String> getMatchingTexts(JsonNode jsonNode) {
    return parseContext.parse(jsonNode).read(MATCHING_TEXT_PATH, LIST_OF_STRINGS);
  }
}
