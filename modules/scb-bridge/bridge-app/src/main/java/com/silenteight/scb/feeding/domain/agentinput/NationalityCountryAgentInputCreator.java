package com.silenteight.scb.feeding.domain.agentinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class NationalityCountryAgentInputCreator implements AgentInput {

  @Override
  public AgentInputIn<Feature> createAgentInput(Alert alert, Match match) {
    return AgentInputIn.builder()
        .alert(alert.details().getAlertName())
        .match(match.details().getMatchName())
        .featureInputs(List.of(
            CountryFeatureInputOut.builder()
                .feature("features/nationalityCountry")
                .alertedPartyCountries(getApCountries(alert.alertedParty()))
                .watchlistCountries(getWlCountries(match.matchedParty()))
                .build()
        ))
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
