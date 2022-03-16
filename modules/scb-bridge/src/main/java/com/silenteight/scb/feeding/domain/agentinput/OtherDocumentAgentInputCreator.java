package com.silenteight.scb.feeding.domain.agentinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OtherDocumentAgentInputCreator implements AgentInput {

  @Override
  public AgentInputIn<Feature> createAgentInput(Alert alert, Match match) {
    return AgentInputIn.builder()
        .alert(alert.details().getAlertName())
        .match(match.details().getMatchName())
        .featureInputs(List.of(
            DocumentFeatureInputOut.builder()
                .feature("features/otherDocument")
                .alertedPartyDocuments(getApOtherDocuments(alert.alertedParty()))
                .watchlistDocuments(getWlOtherDocuments(match.matchedParty()))
                .build()
        ))
        .build();
  }

  private List<String> getApOtherDocuments(AlertedParty alertedParty) {
    Collection<String> apDocOthers = alertedParty.apDocOthers();
    if (CollectionUtils.isNotEmpty(apDocOthers)) {
      return new ArrayList<>(apDocOthers);
    }
    return Collections.emptyList();
  }

  private List<String> getWlOtherDocuments(MatchedParty matchedParty) {
    List<String> documents = new ArrayList<>();
    documents.addAll(matchedParty.wlBicCodes());
    documents.addAll(matchedParty.wlSearchCodes());
    return documents;
  }
}
