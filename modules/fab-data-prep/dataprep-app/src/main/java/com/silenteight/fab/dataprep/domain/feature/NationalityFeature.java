package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class NationalityFeature implements FabFeature {

  static final String FEATURE_NAME = "features/nationalityCountry";
  private static final String JSON_PATH = "$.HittedEntity.Nationality";

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
                .featureInputs(of(CountryFeatureInputOut.builder()
                    .feature(FEATURE_NAME)
                    .alertedPartyCountries(getAlertedPart(registeredAlert))
                    .watchlistCountries(getWatchlistPart(match.getPayload()))
                    .build()))
                .build())
        .collect(toList());
  }

  private List<String> getAlertedPart(RegisteredAlert registeredAlert) {
    return of(registeredAlert.getParsedMessageData().getCountryOfIncorporation());
  }

  protected List<String> getWatchlistPart(JsonNode jsonNode) {
    return of(parseContext
        .parse(jsonNode)
        .read(JSON_PATH, String.class));
  }
}
