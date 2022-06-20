/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match.Flag;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.NeoFlag;

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
    int flags = Flag.NONE.getValue();

    if (isObsolete())
      flags |= Flag.OBSOLETE.getValue();
    if (isSolvedByAnalyst())
      flags |= Flag.SOLVED.getValue();

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

  private boolean isNeoFlagPresent() {
    return nonNull(suspect.getNeoFlag());
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

  private boolean checkLastDecisionBatchId() {
    return isNotBlank(suspect.getBatchId())
        && isNotBlank(lastDecBatchId)
        && suspect.getBatchId().compareTo(lastDecBatchId) <= 0;
  }

  public boolean isNew() {
    return !isObsolete() && !isSolvedByAnalyst();
  }
}
