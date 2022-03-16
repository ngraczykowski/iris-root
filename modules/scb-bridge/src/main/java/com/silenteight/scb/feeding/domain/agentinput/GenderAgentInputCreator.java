package com.silenteight.scb.feeding.domain.agentinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import java.util.Collections;
import java.util.List;

public class GenderAgentInputCreator implements AgentInput {

  @Override
  public AgentInputIn<Feature> createAgentInput(Alert alert, Match match) {
    return AgentInputIn.builder()
        .alert(alert.details().getAlertName())
        .match(match.details().getMatchName())
        .featureInputs(List.of(
            GenderFeatureInputOut.builder()
                .feature("features/gender")
                .alertedPartyGenders(getApGenders(alert.alertedParty()))
                .watchlistGenders(getWlGenders(match.matchedParty()))
                .build()
        ))
        .build();
  }

  private List<String> getApGenders(AlertedParty alertedParty) {
    String apGender = alertedParty.apGender();
    String apNameGender = alertedParty.apGenderFromName();
    return List.of(apGender, apNameGender);
  }

  private List<String> getWlGenders(MatchedParty matchedParty) {
    return Collections.singletonList(matchedParty.wlGender());
  }
}
