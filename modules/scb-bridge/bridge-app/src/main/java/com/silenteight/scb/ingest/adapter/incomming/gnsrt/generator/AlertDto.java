package com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;

import java.util.Collection;

@Builder
@Getter
class AlertDto {

  private final String systemId;
  private final String unit;
  private final String details;
  private final Collection<Suspect> suspects;
}
