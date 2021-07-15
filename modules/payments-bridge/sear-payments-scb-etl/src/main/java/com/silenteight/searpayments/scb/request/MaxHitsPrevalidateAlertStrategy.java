package com.silenteight.searpayments.scb.request;

import com.silenteight.searpayments.scb.domain.Alert;
import lombok.NonNull;
//
//class MaxHitsPrevalidateAlertStrategy implements PrevalidateAlertStrategy {
//
//  final int maxHits;
//
//  MaxHitsPrevalidateAlertStrategy(int maxHits) {
//    if (maxHits <= 0) {
//      throw new IllegalArgumentException("maxHits must be positive number");
//    }
//    this.maxHits = maxHits;
//  }
//
//  @Override
//  @NonNull
//  public PrevalidateResult validate(@NonNull Alert alert) {
//    var hitCount = alert.getHits().size();
//    return hitCount <= maxHits ?
//           PrevalidateResult.ok() :
//           PrevalidateResult.invalid(
//               "MAX_HIT_COUNT_EXCEEDED",
//               "You exceeded the maximum number of hits. I found "
//                   + hitCount + " when only " + maxHits + " are allowed");
//  }
//}
