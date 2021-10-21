package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("matchType")
@RequiredArgsConstructor
class MatchTypeProcess implements CategoryValueProcess {

  @Override
  public CategoryValue extract(
      HitData hitData, String matchName) {
    return CategoryValue
        .newBuilder()
        .setName("categories/matchType")
        .setMatch(matchName)
        .setSingleValue(hitData.getHitAndWlPartyData().getSolutionType().name())
        .build();
  }

}
