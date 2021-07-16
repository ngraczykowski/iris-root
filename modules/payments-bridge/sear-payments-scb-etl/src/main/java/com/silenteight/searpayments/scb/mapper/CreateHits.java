package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.domain.Hit;
import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;
import com.silenteight.searpayments.scb.etl.response.HitData;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
class CreateHits {

  @NonNull private final CreateHitFactory createHitFactory;
  @NonNull private final AlertEtlResponse alertEtlResponse;

  Collection<Hit> create() {
    Collection<Hit> hits = new ArrayList<>();
    int hitIndex = 1;
    for (HitData hitData : alertEtlResponse.getHits()) {
      hits.add(createHitFactory.create(hitData, hitIndex).create());
      hitIndex++;
    }
    return hits;
  }
}
