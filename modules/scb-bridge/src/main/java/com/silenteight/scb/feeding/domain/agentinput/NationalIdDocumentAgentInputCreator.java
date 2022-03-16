package com.silenteight.scb.feeding.domain.agentinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.nationalid.v1.NationalIdFeatureInputOut;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NationalIdDocumentAgentInputCreator implements AgentInput {

  @Override
  public AgentInputIn<Feature> createAgentInput(Alert alert, Match match) {
    return AgentInputIn.builder()
        .alert(alert.details().getAlertName())
        .match(match.details().getMatchName())
        .featureInputs(List.of(
            NationalIdFeatureInputOut.builder()
                .feature("features/nationalIdDocument")
                .alertedPartyDocumentNumbers(alert.alertedParty().apDocNationalIds())
                .watchlistDocumentNumbers(match.matchedParty().wlNationalIds())
                .alertedPartyCountries(getApCountries(alert.alertedParty()))
                .watchlistCountries(getWlCountries(match.matchedParty()))
                .build()
        ))
        .build();
  }

  private List<String> getApCountries(AlertedParty alertedParty) {
    return Collections.singletonList(alertedParty.apBookingLocation());
  }

  private List<String> getWlCountries(MatchedParty matchedParty) {
    return Arrays.asList(matchedParty.wlCountry(), matchedParty.wlDesignation());
  }
}
