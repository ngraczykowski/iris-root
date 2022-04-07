package com.silenteight.scb.feeding.domain.agentinput.feature;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class NationalityFeatureFactory implements FeatureFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    return CountryFeatureInputOut.builder()
                .feature("features/nationality")
                .alertedPartyCountries(getApCountries(alert.alertedParty()))
                .watchlistCountries(getWlCountries(match.matchedParty()))
                .build();
  }

  private List<String> getApCountries(AlertedParty alertedParty) {
    List<String> apCountries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(apCountries, alertedParty.apNationality());
    return apCountries;
  }

  private List<String> getWlCountries(MatchedParty matchedParty) {
    List<String> wlCountries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(wlCountries, matchedParty.wlNationality());
    return wlCountries;
  }
}
