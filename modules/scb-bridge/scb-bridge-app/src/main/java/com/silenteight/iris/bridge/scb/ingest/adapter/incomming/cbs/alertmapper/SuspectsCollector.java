/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

@RequiredArgsConstructor
class SuspectsCollector {

  private final HitDetailsParser hitDetailsParser;

  Collection<Suspect> collect(
      @Nullable String details,
      @NonNull List<CbsHitDetails> cbsHitDetails) {

    HitDetails hitDetails = hitDetailsParser.parse(details);
    fillSuspectData(hitDetails, cbsHitDetails);

    return hitDetails.extractUniqueSuspects();
  }

  private static void fillSuspectData(HitDetails hitDetails, List<CbsHitDetails> cbsHitDetails) {
    for (Suspect suspect : hitDetails.getSuspects()) {
      if (!cbsHitDetails.isEmpty())
        suspect.loadSuspectWithNeoFlag(cbsHitDetails);
    }
  }
}
