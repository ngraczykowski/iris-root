package com.silenteight.scb.feeding.domain.agentinput.feature;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class GenderFeatureFactory implements FeatureFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    return GenderFeatureInputOut.builder()
                .feature("features/gender")
                .alertedPartyGenders(getApGenders(alert.alertedParty()))
                .watchlistGenders(getWlGenders(match.matchedParty()))
                .build();
  }

  private List<String> getApGenders(AlertedParty alertedParty) {
    List<String> apGenders = new ArrayList<>();
    CollectionUtils.addIgnoreNull(apGenders, alertedParty.apGender());
    CollectionUtils.addIgnoreNull(apGenders, alertedParty.apGenderFromName());
    return apGenders;
  }

  private List<String> getWlGenders(MatchedParty matchedParty) {
    List<String> wlGenders = new ArrayList<>();
    CollectionUtils.addIgnoreNull(wlGenders, matchedParty.wlGender());
    return wlGenders;
  }
}
