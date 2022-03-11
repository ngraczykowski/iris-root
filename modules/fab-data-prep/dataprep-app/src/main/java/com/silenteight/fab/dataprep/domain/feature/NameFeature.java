package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.name.v1.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class NameFeature implements FabFeature {

  static final String FEATURE_NAME = "features/name";
  private static final String WATCH_LIST_NAME_PATH = "$.HittedEntity.Names[*].Name";
  private static final String MATCHING_TEXT_PATH = "$.MatchingText";

  private final ParseContext parseContext;

  @Override
  public List<AgentInputIn<Feature>> createFeatureInput(FeatureInputsCommand featureInputsCommand) {
    RegisteredAlert registeredAlert = featureInputsCommand.getRegisteredAlert();
    ParsedMessageData parsedMessageData = registeredAlert.getParsedMessageData();
    return registeredAlert.getMatches()
        .stream()
        .map(match ->
            AgentInputIn.builder()
                .match(match.getMatchName())
                .alert(registeredAlert.getAlertName())
                .featureInputs(of(NameFeatureInputOut.builder()
                    .feature(FEATURE_NAME)
                    .alertedPartyNames(getAlertedPart(parsedMessageData))
                    .alertedPartyType(getPartyType(parsedMessageData))
                    .watchlistNames(getWatchlistPart(match.getPayload()))
                    .matchingTexts(getMatchingTexts(match.getPayload()))
                    .build()))
                .build())
        .collect(toList());
  }

  private List<AlertedPartyNameOut> getAlertedPart(ParsedMessageData parsedMessageData) {
    return Stream.of(parsedMessageData.getName(), parsedMessageData.getShortName())
        .map(this::getAlertedPartyNameOut)
        .collect(toList());
  }

  private AlertedPartyNameOut getAlertedPartyNameOut(String name) {
    return AlertedPartyNameOut.builder()
        .name(name)
        .build();
  }

  protected List<WatchlistNameOut> getWatchlistPart(JsonNode jsonNode) {
    TypeRef<List<String>> typeRef = new TypeRef<>() {};

    return parseContext
        .parse(jsonNode)
        .read(WATCH_LIST_NAME_PATH, typeRef)
        .stream()
        .map(name -> getWatchlistNameOut(name, NameTypeOut.REGULAR))
        .collect(toList());

    //TODO add alias names
  }

  private WatchlistNameOut getWatchlistNameOut(String name, NameTypeOut type) {
    return WatchlistNameOut.builder()
        .name(name)
        .type(type)
        .build();
  }

  private EntityTypeOut getPartyType(ParsedMessageData parsedMessageData) {
    switch (parsedMessageData.getCustomerTypeAsEnum()) {
      case INDIVIDUAL:
        return EntityTypeOut.INDIVIDUAL;
      case ORGANIZATION:
        return EntityTypeOut.ORGANIZATION;
      case ENTITY_TYPE_UNSPECIFIED:
      default:
        return EntityTypeOut.ENTITY_TYPE_UNSPECIFIED;
    }
  }

  private List<String> getMatchingTexts(JsonNode jsonNode) {
    String value = parseContext.parse(jsonNode).read(MATCHING_TEXT_PATH, String.class);
    return value == null ? emptyList() : of(value);
  }
}
