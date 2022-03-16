package com.silenteight.scb.feeding.domain.agentinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;

import java.util.Collections;
import java.util.List;

public class PassportNumberDocumentAgentInputCreator implements AgentInput {

  @Override
  public AgentInputIn<Feature> createAgentInput(Alert alert, Match match) {
    return AgentInputIn.builder()
        .alert(alert.details().getAlertName())
        .match(match.details().getMatchName())
        .featureInputs(List.of(
            DocumentFeatureInputOut.builder()
                .feature("features/passportNumberDocument")
                .alertedPartyDocuments(getApPassportNumberDocuments(alert.alertedParty()))
                .watchlistDocuments(getWlPassportNumberDocuments(match.matchedParty()))
                .build()
        ))
        .build();
  }

  private List<String> getApPassportNumberDocuments(AlertedParty alertedParty) {
    return alertedParty.apDocPassports().stream().toList();
  }

  private List<String> getWlPassportNumberDocuments(MatchedParty matchedParty) {
    return Collections.singletonList(matchedParty.wlPassport());
  }
}
