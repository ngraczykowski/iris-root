package com.silenteight.customerbridge.cbs.alertmapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.domain.CbsHitDetails;
import com.silenteight.customerbridge.common.hitdetails.HitDetailsParser;
import com.silenteight.customerbridge.common.hitdetails.model.HitDetails;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;

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
