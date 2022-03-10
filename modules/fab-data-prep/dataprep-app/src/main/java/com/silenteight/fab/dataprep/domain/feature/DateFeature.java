package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut;
import com.silenteight.universaldatasource.api.library.date.v1.EntityTypeOut;
import com.silenteight.universaldatasource.api.library.date.v1.SeverityModeOut;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;

import java.util.List;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DateFeature implements FabFeature {

  static final String FEATURE_NAME = "features/date";
  private static final String JSON_PATH = "$.HittedEntity.DatesOfBirth[*].DateOfBirth";

  private final ParseContext parseContext;

  @Override
  public List<AgentInputIn<Feature>> createFeatureInput(FeatureInputsCommand featureInputsCommand) {
    ExtractedAlert extractedAlert = featureInputsCommand.getExtractedAlert();
    ParsedMessageData parsedMessageData = extractedAlert.getParsedMessageData();
    return featureInputsCommand.getExtractedAlert().getMatches()
        .stream()
        .map(match ->
            AgentInputIn.builder()
                .match(match.getMatchName())
                .alert(extractedAlert.getAlertName())
                .featureInputs(of(DateFeatureInputOut.builder()
                    .feature(FEATURE_NAME)
                    .alertedPartyDates(getAlertedPart(parsedMessageData))
                    .watchlistDates(getWatchlistPart(match.getPayload()))
                    .alertedPartyType(getPartyType(parsedMessageData))
                    .mode(SeverityModeOut.NORMAL)   //TODO is it correct?
                    .build()))
                .build())
        .collect(toList());
  }

  private List<String> getAlertedPart(ParsedMessageData parsedMessageData) {
    return of(parsedMessageData.getDob());
  }

  protected List<String> getWatchlistPart(JsonNode jsonNode) {
    TypeRef<List<String>> typeRef = new TypeRef<>() {};

    return parseContext
        .parse(jsonNode)
        .read(JSON_PATH, typeRef);
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
}
