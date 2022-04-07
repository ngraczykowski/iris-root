package com.silenteight.scb.feeding.domain.agentinput.feature;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.nationalid.v1.NationalIdFeatureInputOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class NationalIdFeatureFactory implements FeatureFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    return NationalIdFeatureInputOut.builder()
                .feature("features/nationalId")
                .alertedPartyDocumentNumbers(alert.alertedParty().apDocNationalIds())
                .watchlistDocumentNumbers(match.matchedParty().wlNationalIds())
                .alertedPartyCountries(getApCountries(alert.alertedParty()))
                .watchlistCountries(getWlCountries(match.matchedParty()))
                .build();
  }

  private List<String> getApCountries(AlertedParty alertedParty) {
    List<String> apCountries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(apCountries, alertedParty.apBookingLocation());
    return apCountries;
  }

  private List<String> getWlCountries(MatchedParty matchedParty) {
    List<String> wlCountries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(wlCountries, matchedParty.wlDesignation());
    return wlCountries;
  }
}
