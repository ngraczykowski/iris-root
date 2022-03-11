package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class GenderFeature implements FabFeature {

  static final String FEATURE_NAME = "features/gender";

  //TODO change this to correct path
  private static final String JSON_PATH = "$.HittedEntity.Gender";

  private final ParseContext parseContext;

  @Override
  public List<AgentInputIn<Feature>> createFeatureInput(FeatureInputsCommand featureInputsCommand) {
    RegisteredAlert registeredAlert = featureInputsCommand.getRegisteredAlert();
    return registeredAlert.getMatches()
        .stream()
        .map(match ->
            AgentInputIn.builder()
                .match(match.getMatchName())
                .alert(registeredAlert.getAlertName())
                .featureInputs(of(GenderFeatureInputOut.builder()
                    .feature(FEATURE_NAME)
                    .alertedPartyGenders(getAlertedPart(registeredAlert))
                    .watchlistGenders(getWatchlistPart(match.getPayload()))
                    .build()))
                .build())
        .collect(toList());
  }

  private static List<String> getAlertedPart(RegisteredAlert registeredAlert) {
    return of(registeredAlert.getParsedMessageData().getGender());
  }

  private List<String> getWatchlistPart(JsonNode jsonNode) {
    String value = parseContext.parse(jsonNode).read(JSON_PATH, String.class);
    return value == null ? emptyList() : of(value);
  }

}
