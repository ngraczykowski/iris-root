package com.silenteight.scb.feeding.domain.agent.input.feature;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DocumentNumberFeatureFactory implements FeatureFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    return DocumentFeatureInputOut.builder()
                .feature("features/document")
                .alertedPartyDocuments(getApOtherDocuments(alert.alertedParty()))
                .watchlistDocuments(getWlOtherDocuments(match.matchedParty()))
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
    documents.addAll(getWlBicCodes(matchedParty));
    documents.addAll(getWlSearchCodes(matchedParty));
    return documents;
  }

  private List<String> getWlBicCodes(MatchedParty matchedParty) {
    Collection<String> wlBicCodes = matchedParty.wlBicCodes();
    if (CollectionUtils.isNotEmpty(wlBicCodes)) {
      return new ArrayList<>(wlBicCodes);
    }
    return Collections.emptyList();
  }

  private List<String> getWlSearchCodes(MatchedParty matchedParty) {
    Collection<String> wlSearchCodes = matchedParty.wlSearchCodes();
    if (CollectionUtils.isNotEmpty(wlSearchCodes)) {
      return new ArrayList<>(wlSearchCodes);
    }
    return Collections.emptyList();
  }
}
