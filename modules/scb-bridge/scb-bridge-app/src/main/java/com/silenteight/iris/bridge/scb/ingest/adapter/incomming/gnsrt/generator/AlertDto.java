/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;

import java.util.Collection;

@Builder
@Getter
class AlertDto {

  private final String systemId;
  private final String unit;
  private final String details;
  private final Collection<Suspect> suspects;
}
