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

public class PassportNumberDocumentFeatureFactory implements FeatureFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    return DocumentFeatureInputOut.builder()
                .feature("features/passport")
                .alertedPartyDocuments(getApPassportNumberDocuments(alert.alertedParty()))
                .watchlistDocuments(getWlPassportNumberDocuments(match.matchedParty()))
                .build();
  }

  private List<String> getApPassportNumberDocuments(AlertedParty alertedParty) {
    Collection<String> apDocPassports = alertedParty.apDocPassports();
    if (CollectionUtils.isNotEmpty(apDocPassports)) {
      return new ArrayList<>(apDocPassports);
    }
    return Collections.emptyList();
  }

  private List<String> getWlPassportNumberDocuments(MatchedParty matchedParty) {
    List<String> wlPassportNumberDocuments = new ArrayList<>();
    CollectionUtils.addIgnoreNull(wlPassportNumberDocuments, matchedParty.wlPassport());
    return wlPassportNumberDocuments;
  }
}
