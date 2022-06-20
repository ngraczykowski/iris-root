/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

@Getter
@Builder
public class MatchDetails {

  private final Collection<String> matchedApNames;
  private final Set<String> matchingTexts;
  @Setter
  private String matchName;
}
