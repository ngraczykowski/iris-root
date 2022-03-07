package com.silenteight.fab.dataprep.domain.feature;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert.Match;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class GenderFeature implements FabFeature<GenderFeatureInputOut> {

  @Override
  public BatchCreateAgentInputsIn<GenderFeatureInputOut> createFeatureInput(
      FeatureInputsCommand featureInputsCommand) {
    var agentInputInList = featureInputsCommand.getExtractedAlert().getMatches()
        .stream()
        .map(Match::getMatchName)
        .map(matchName ->
            AgentInputIn.<GenderFeatureInputOut>builder()
                .match(matchName)
                .alert(featureInputsCommand.getExtractedAlert().getAlertName())
                .featureInputs(List.of(GenderFeatureInputOut.builder()
                    .feature("features/gender")
                    .alertedPartyGenders(List.of("M"))
                    .watchlistGenders(List.of("F"))
                    .build()))
                .build())
        .collect(toList());

    return toAgentInputs(agentInputInList);
  }
}
