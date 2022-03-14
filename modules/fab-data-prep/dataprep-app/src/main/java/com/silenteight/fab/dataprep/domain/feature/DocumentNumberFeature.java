package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DocumentNumberFeature implements FabFeature {

  static final String FEATURE_NAME = "features/otherDocument";
  private static final String JSON_PATH = "$.HittedEntity.OtherDocument";    //TODO is it correct?

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
                .featureInputs(of(DocumentFeatureInputOut.builder()
                    .feature(FEATURE_NAME)
                    .alertedPartyDocuments(getAlertedPart(parsedMessageData))
                    .watchlistDocuments(getWatchlistPart(match.getPayload()))
                    .build()))
                .build())
        .collect(toList());
  }

  private List<String> getAlertedPart(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getSwiftBic(),
        parsedMessageData.getPassportNum(),
        parsedMessageData.getNationalId());
  }

  protected List<String> getWatchlistPart(JsonNode jsonNode) {
    TypeRef<List<String>> typeRef = new TypeRef<>() {};

    List<String> list = parseContext
        .parse(jsonNode)
        .read(JSON_PATH, typeRef);

    return Optional.ofNullable(list).orElse(emptyList());
  }
}
