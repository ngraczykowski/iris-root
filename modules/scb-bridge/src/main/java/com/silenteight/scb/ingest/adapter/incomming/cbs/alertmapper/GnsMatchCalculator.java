package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.alert.Match.Flags;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.NeoFlag;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;

import javax.annotation.Nullable;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.lowerCase;

@RequiredArgsConstructor
public class GnsMatchCalculator {

  private static final String OBSOLETE = "obsolete";

  private final Suspect suspect;
  @Nullable
  private final String lastDecBatchId;
  private final boolean hasLastDecision;

  public int calculateFlags() {
    int flags = Flags.FLAG_NONE_VALUE;

    if (isObsolete())
      flags |= Flags.FLAG_OBSOLETE_VALUE;
    if (isSolvedByAnalyst())
      flags |= Flags.FLAG_SOLVED_VALUE;

    return flags;
  }

  private boolean isObsolete() {
    if (isNeoFlagPresent())
      return NeoFlag.OBSOLETE == suspect.getNeoFlag();

    if (isNull(suspect.getBatchId()))
      return true;

    String lowerCaseBatchId = lowerCase(suspect.getBatchId());
    return contains(lowerCaseBatchId, OBSOLETE);
  }

  private boolean isSolvedByAnalyst() {
    if (isNeoFlagPresent())
      return NeoFlag.EXISTING == suspect.getNeoFlag();

    if (isObsolete())
      return false;

    return hasLastDecision
        && checkLastDecisionBatchId()
        && !isObsolete();
  }

  public boolean isNew() {
    return !isObsolete() && !isSolvedByAnalyst();
  }

  private boolean checkLastDecisionBatchId() {
    return isNotBlank(suspect.getBatchId())
        && isNotBlank(lastDecBatchId)
        && suspect.getBatchId().compareTo(lastDecBatchId) <= 0;
  }

  private boolean isNeoFlagPresent() {
    return nonNull(suspect.getNeoFlag());
  }
}
