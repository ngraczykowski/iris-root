package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.fab.dataprep.domain.model.ParsedPayload;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.name.v1.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;

import java.util.List;
import java.util.stream.Stream;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class NameFeature implements FabFeature {

  static final String FEATURE_NAME = "features/name";
  private static final String WATCH_LIST_NAME_PATH = "$.HittedEntity.Names[*].Name";
  private static final String TYPE_PATH = "$.HittedEntity.Type";
  private static final String MATCHING_TEXT_PATH = "$.MatchingText";

  private final ParseContext parseContext;

  @Override
  public List<AgentInputIn<Feature>> createFeatureInput(FeatureInputsCommand featureInputsCommand) {
    ExtractedAlert extractedAlert = featureInputsCommand.getExtractedAlert();
    return featureInputsCommand.getExtractedAlert().getMatches()
        .stream()
        .map(match ->
            AgentInputIn.builder()
                .match(match.getMatchName())
                .alert(extractedAlert.getAlertName())
                .featureInputs(of(NameFeatureInputOut.builder()
                    .feature(FEATURE_NAME)
                    .alertedPartyNames(getAlertedPart(extractedAlert))
                    .watchlistNames(getWatchlistPart(match.getPayload()))
                    .alertedPartyType(getPartyType(match.getPayload()))
                    .matchingTexts(getMatchingTexts(match.getPayload()))
                    .build()))
                .build())
        .collect(toList());
  }

  private List<AlertedPartyNameOut> getAlertedPart(ExtractedAlert extractedAlert) {
    ParsedPayload parsedPayload = extractedAlert.getParsedPayload();
    return Stream.of(parsedPayload.getName(), parsedPayload.getShortName())
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

  protected EntityTypeOut getPartyType(JsonNode jsonNode) {
    String type = parseContext.parse(jsonNode).read(TYPE_PATH, String.class);
    if (type == null || type.isBlank()) {
      return EntityTypeOut.ENTITY_TYPE_UNSPECIFIED;
    } else if ("I".equalsIgnoreCase(type)) {
      return EntityTypeOut.INDIVIDUAL;
    } else {
      return EntityTypeOut.ORGANIZATION;
    }
  }

  private List<String> getMatchingTexts(JsonNode jsonNode) {
    return of(parseContext.parse(jsonNode).read(MATCHING_TEXT_PATH, String.class));
  }
}
