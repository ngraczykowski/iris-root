package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

class WatchlistTypeExtractor implements CategoryValueExtractor {

  @Override
  public CategoryValue extract(EtlHit etlHit, RegisterAlertResponse registeredAlert) {
    return CategoryValue
        .newBuilder()
        .setName("categories/watchlistType")
        .setAlert(registeredAlert.getAlertName())
        .setMatch(registeredAlert.getMatchName(etlHit.getMatchId()))
        .setSingleValue(etlHit.getWatchlistType().getName())
        .build();
  }
}
