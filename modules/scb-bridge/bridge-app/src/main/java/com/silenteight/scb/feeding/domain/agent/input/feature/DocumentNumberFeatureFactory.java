package com.silenteight.scb.feeding.domain.agent.input.feature;

import com.silenteight.scb.feeding.infrastructure.util.CollectionUtils;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;

import java.util.ArrayList;
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

  private static List<String> getApOtherDocuments(AlertedParty alertedParty) {
    var result = CollectionUtils.addAllIfNotNull(new ArrayList<>(), alertedParty.apDocOthers());
    result = CollectionUtils.addAllIfNotNull(result, alertedParty.apDocNationalIds());
    return CollectionUtils.addAllIfNotNull(result, alertedParty.apDocPassports());
  }

  private static List<String> getWlOtherDocuments(MatchedParty matchedParty) {
    var documents = CollectionUtils.addAllIfNotNull(new ArrayList<>(), matchedParty.wlBicCodes());
    documents = CollectionUtils.addAllIfNotNull(documents, matchedParty.wlSearchCodes());
    documents = CollectionUtils.addIfNotNull(documents, matchedParty.wlNationalId());
    return CollectionUtils.addIfNotNull(documents, matchedParty.wlPassport());
  }
}
