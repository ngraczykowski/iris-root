package com.silenteight.fab.dataprep.domain.feature;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert.Match;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import java.util.List;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
public class GenderFeature implements FabFeature {

  private static final String FEATURE_NAME = "features/gender";

  @Override
  public List<AgentInputIn<Feature>> createFeatureInput(FeatureInputsCommand featureInputsCommand) {
    ExtractedAlert extractedAlert = featureInputsCommand.getExtractedAlert();
    return featureInputsCommand.getExtractedAlert().getMatches()
        .stream()
        .map(Match::getMatchName)
        .map(matchName ->
            AgentInputIn.builder()
                .match(matchName)
                .alert(extractedAlert.getAlertName())
                .featureInputs(of(GenderFeatureInputOut.builder()
                    .feature(FEATURE_NAME)
                    .alertedPartyGenders(getAlertedPartGenders(extractedAlert))
                    .watchlistGenders(of("F"))
                    .build()))
                .build())
        .collect(toList());
  }

  private List<String> getAlertedPartGenders(ExtractedAlert extractedAlert) {
    return of(extractedAlert.getParsedPayload().getGender());
  }

}
