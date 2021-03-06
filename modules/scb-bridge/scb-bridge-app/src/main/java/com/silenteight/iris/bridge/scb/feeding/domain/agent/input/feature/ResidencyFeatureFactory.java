/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.agent.input.feature;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ResidencyFeatureFactory implements FeatureFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    return CountryFeatureInputOut.builder()
        .feature("features/residency")
        .alertedPartyCountries(getApCountries(alert.alertedParty()))
        .watchlistCountries(getWlCountries(match.matchedParty()))
        .build();
  }

  private List<String> getApCountries(AlertedParty alertedParty) {
    List<String> apResidencyCountries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(apResidencyCountries, alertedParty.apResidence());
    apResidencyCountries.addAll(getApResidenceSynonyms(alertedParty));
    apResidencyCountries.addAll(getApResidentialAddresses(alertedParty));
    return apResidencyCountries;
  }

  private List<String> getApResidenceSynonyms(AlertedParty alertedParty) {
    Collection<String> apResidenceSynonyms = alertedParty.apResidenceSynonyms();
    if (CollectionUtils.isNotEmpty(apResidenceSynonyms)) {
      return new ArrayList<>(apResidenceSynonyms);
    }
    return Collections.emptyList();
  }

  private List<String> getApResidentialAddresses(AlertedParty alertedParty) {
    Collection<String> apResidentialAddresses = alertedParty.apResidentialAddresses();
    if (CollectionUtils.isNotEmpty(apResidentialAddresses)) {
      return new ArrayList<>(apResidentialAddresses);
    }
    return Collections.emptyList();
  }

  private List<String> getWlCountries(MatchedParty matchedParty) {
    List<String> wlCountries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(wlCountries, matchedParty.wlResidence());
    return wlCountries;
  }
}
