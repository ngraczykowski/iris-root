package com.silenteight.scb.feeding.domain.agentinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut;
import com.silenteight.universaldatasource.api.library.date.v1.EntityTypeOut;
import com.silenteight.universaldatasource.api.library.date.v1.SeverityModeOut;

import java.util.Collections;
import java.util.List;

public class DateAgentInputCreator implements AgentInput {

  @Override
  public AgentInputIn<Feature> createAgentInput(Alert alert, Match match) {
    return AgentInputIn.builder()
        .alert(alert.details().getAlertName())
        .match(match.details().getMatchName())
        .featureInputs(List.of(
            DateFeatureInputOut.builder()
                .feature("features/dateOfBirth")
                .alertedPartyDates(Collections.singletonList(alert.alertedParty().apDobDoi()))
                .watchlistDates(Collections.singletonList(match.matchedParty().wlDob()))
                .alertedPartyType(determineApType(match.matchedParty().apType()))
                .mode(SeverityModeOut.NORMAL)
                .build()))
        .build();
  }

  private static EntityTypeOut determineApType(String apType) {
    if (apType.equals("I")) {
      return EntityTypeOut.INDIVIDUAL;
    } else if (apType.equals("C")) {
      return EntityTypeOut.ORGANIZATION;
    }
    return EntityTypeOut.ENTITY_TYPE_UNSPECIFIED;
  }
}
